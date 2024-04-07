package com.kapturecrm.nlpdashboardservice.service;

import com.kapturecrm.nlpdashboardservice.dto.NlpDashboardReqDto;
import com.kapturecrm.nlpdashboardservice.dto.NlpDashboardResponse;
import com.kapturecrm.nlpdashboardservice.exception.KaptureException;
import com.kapturecrm.nlpdashboardservice.model.NlpDashboardPrompt;
import com.kapturecrm.nlpdashboardservice.repository.MysqlRepo;
import com.kapturecrm.nlpdashboardservice.repository.NlpDashboardRepository;
import com.kapturecrm.nlpdashboardservice.utility.BaseResponse;
import com.kapturecrm.nlpdashboardservice.utility.NlpDashboardUtils;
import com.kapturecrm.object.PartnerUser;
import com.kapturecrm.session.SessionManager;
import com.kapturecrm.utilobj.CommonUtils;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

import static com.kapturecrm.nlpdashboardservice.utility.ConversionUtil.getTimestampForSql;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NlpDashboardService {

    @Value("${openai.apiKey}")
    private String apiKey;

    private final NlpDashboardRepository nlpDashboardRepository;
    private final NlpDashboardUtils nlpDashboardUtils;
    private final HttpServletRequest httpServletRequest;
    private final MysqlRepo mysqlRepo;

    public ResponseEntity<?> generateNlpDashboard(NlpDashboardReqDto reqDto) {
        try {
            NlpDashboardResponse resp = new NlpDashboardResponse();
            PartnerUser partnerUser = SessionManager.getPartnerUser(httpServletRequest);
            int cmId = partnerUser != null ? partnerUser.getCmId() : 415;
            int empId = partnerUser != null ? partnerUser.getEmpId() : 415;

            NlpDashboardPrompt nlpDashboardprompt = new NlpDashboardPrompt();
            setData(nlpDashboardprompt, cmId, empId, reqDto);
            Thread promptSaveThread = new Thread(() -> mysqlRepo.addPrompt(nlpDashboardprompt));
            promptSaveThread.start();

            OpenAiChatModel openAiModel = OpenAiChatModel.withApiKey(apiKey);
            String aiReply = openAiModel.generate(getPromptForAI(cmId, reqDto));
            String finalSql = validateAIGeneratedSQL(cmId, aiReply);
            log.info("FINAL-NLP-SQL: {}", finalSql);
            System.out.println(finalSql);
            List<LinkedHashMap<String, Object>> values = nlpDashboardRepository.findNlpDashboardDataFromSql(finalSql);

            switch (reqDto.getDashboardType().toLowerCase()) {
                case "text" -> {
                    String textResp = openAiModel.generate("prompt: " + reqDto.getPrompt() +
                            " data: " + JSONArray.fromObject(values).toString() +
                            " for above prompt give me a detail text response in less than 120 words by analyzing the data ");
                    resp.setTextResponse(textResp);
                }
            }

            if (!values.isEmpty()) {
                resp.setDashboardColumns(values.get(0).keySet());
            }
            resp.setDashboardValues(values);

            promptSaveThread.join();
            resp.setPromptId(nlpDashboardprompt.getId());

            return BaseResponse.success(resp);
        } catch (KaptureException ke) {
            log.warn("Error in generateNlpDashboard" + ke.getBaseResponse());
            return ke.getBaseResponse();
        } catch (Exception e) {
            log.error("Error in generateNlpDashboard", e);
            return BaseResponse.error(e);
        }
    }

    private void setData(NlpDashboardPrompt nlpDashboardprompt, int cmId, int empId, NlpDashboardReqDto reqDto) {
        nlpDashboardprompt.setCmId(cmId);
        nlpDashboardprompt.setPrompt(reqDto.getPrompt());
        nlpDashboardprompt.setCreateTime(CommonUtils.getCurrentTimestamp());
        nlpDashboardprompt.setEmpId(empId);
        nlpDashboardprompt.setDashboardType(reqDto.getDashboardType());
    }


    private String validateAIGeneratedSQL(int cmId, String aiReply) throws KaptureException {
        // todo validate reply if it has only sql its fine, else filter out sql alone check for ``` or ```sql
        String sql = aiReply.replaceAll("[\n;]", " ");
        if (!(sql.startsWith("SELECT") || sql.startsWith("select"))) {
            throw new KaptureException(BaseResponse.error(HttpStatus.UNPROCESSABLE_ENTITY, "Only select operation supported!"));
        }
        if (!(sql.contains("WHERE") || sql.contains("where"))) {
            sql += " where cm_id = " + cmId;
        } else if (sql.contains("WHERE") && !sql.split(" WHERE ")[1].contains("cm_id")
                || sql.contains("where") && sql.split(" where ")[1].contains("cm_id")) {
            sql = sql.replace("where", "where cm_id = " + cmId + " and ");
        }
        if (!(sql.contains("LIMIT") || sql.contains("limit"))) {
            sql += " LIMIT 10000";
        }
        return sql;
    }

    private String getPromptForAI(int cmId, NlpDashboardReqDto reqDto) {
        StringBuilder promptBuilder = new StringBuilder();

        // Initial prompt instructions
        promptBuilder.append("Provide a ClickHouse SQL query with correct syntax and column names based on the table schema and prompt, ready to execute directly in ClickHouse.\n");

        // Conditionally add instructions based on dashboard type
        if (reqDto.getDashboardType().equalsIgnoreCase("table") || reqDto.getDashboardType().equalsIgnoreCase("text")) {
            promptBuilder.append("Select fewer than 15 essential columns.");
        } else {
            promptBuilder.append("Select the required columns for creating a ")
                    .append(reqDto.getDashboardType())
                    .append(" visualization. Add alias names (e.g., `column_name as alias`). The column used for the alias 'value' must be of numeric datatype. Multiple types may exist, so the 'value' can be calculated accordingly.\n");
        }

        // Common instructions
        promptBuilder.append("\nExclude columns like 'id', 'cm_id', and foreign key columns.");
        promptBuilder.append("\nInclude 'cm_id = ").append(cmId).append("' in the WHERE clause condition.");

        // Additional information
        JSONObject dbSchema = new JSONObject();
        NlpDashboardUtils.PromptInfo promptInfo = nlpDashboardUtils.convertTableNameAndFindDBSchema(reqDto.getPrompt(), dbSchema);
        promptBuilder.append("\n\nPROMPT: ").append(promptInfo.prompt());
        if (reqDto.getStartDate() != null && reqDto.getEndDate() != null) {
            promptBuilder.append("\nDATE RANGE: ").append(getTimestampForSql(reqDto.getStartDate()))
                    .append(" to ").append(getTimestampForSql(reqDto.getEndDate()));
        }
        promptBuilder.append("\nDB TABLES SCHEMA: ").append(dbSchema);

        return promptBuilder.toString();
    }
}
