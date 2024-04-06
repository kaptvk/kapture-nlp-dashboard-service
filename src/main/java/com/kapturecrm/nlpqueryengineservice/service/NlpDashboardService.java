package com.kapturecrm.nlpqueryengineservice.service;

import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardReqDto;
import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardResponse;
import com.kapturecrm.nlpqueryengineservice.repository.NlpDashboardRepository;
import com.kapturecrm.nlpqueryengineservice.utility.BaseResponse;
import com.kapturecrm.nlpqueryengineservice.utility.NlpDashboardUtils;
import com.kapturecrm.object.PartnerUser;
import com.kapturecrm.session.SessionManager;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

import static com.kapturecrm.nlpqueryengineservice.utility.ConversionUtil.getTimestampForSql;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NlpDashboardService {

    @Value("${openai.apiKey}")
    private String apiKey;

    private final NlpDashboardRepository nlpDashboardRepository;
    private final NlpDashboardUtils nlpDashboardUtils;
    private final HttpServletRequest httpServletRequest;
    private final BaseResponse baseResponse;

    public ResponseEntity<?> generateNlpDashboard(NlpDashboardReqDto reqDto) {
        PartnerUser partnerUser = SessionManager.getPartnerUser(httpServletRequest);
        int cmId = partnerUser != null ? partnerUser.getCmId() : 0;

        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String aiReply = model.generate(getPromptForAI(cmId, reqDto));
        String sql = validateAIGeneratedSQL(cmId, aiReply);
        log.info("finalSql: {}", sql);
        System.out.println("finalSql " + sql);
        List<LinkedHashMap<String, Object>> values = nlpDashboardRepository.findNlpDashboardDataFromSql(sql);

        NlpDashboardResponse resp = new NlpDashboardResponse();
        switch (reqDto.getDashboardType().toLowerCase()) {
            case "text" -> {
                String textResp = model.generate(
                        "prompt: " + reqDto.getPrompt() +
                                " data: " + JSONArray.fromObject(values).toString() +
                                " for above prompt give me a detail text response within 120 words by analyzing the data "
                );
                System.out.println(textResp);
                resp.setTextResponse(textResp);
                if (!values.isEmpty()) {
                    resp.setDashboardColumns(values.get(0).keySet());
                }
                resp.setDashboardValues(values);
            }
            case "table" -> {
                if (!values.isEmpty()) {
                    resp.setDashboardColumns(values.get(0).keySet());
                }
                resp.setDashboardValues(values);
            }
            default -> {
                // chart
            }
        }
        return baseResponse.successResponse(resp);
    }

    private String validateAIGeneratedSQL(int cmId, String aiReply) {
        // todo validate reply if it has only sql its fine, else filter out sql alone check for ``` or ```sql
        String sql = aiReply.replaceAll("[\n;]", " ");
        if (!(sql.contains("WHERE") || sql.contains("where"))) {
            sql += " where cm_id = " + cmId;
        } else if (sql.contains("WHERE") && !sql.split(" WHERE ")[1].contains("cm_id")
                || sql.contains("where") && sql.split(" where ")[1].contains("cm_id")) {
            sql = sql.replace("where", "where cm_id = " + cmId + " and ");
        }
        // todo if date where clause is not present then add limit 1000
        return sql;
    }

    private String getPromptForAI(int cmId, NlpDashboardReqDto reqDto) {
        String prompt = "give ClickHouse sql query with less than 15 essential columns and exclude columns: id, cm_id and include cm_id = " + cmId + " in where clause";
        JSONObject dbSchema = new JSONObject();
        NlpDashboardUtils.PromptInfo promptInfo = nlpDashboardUtils.convertTableNameAndFindDBSchema(reqDto.getPrompt(), dbSchema);
        prompt += "\nfor prompt: " + promptInfo.prompt();
        if (reqDto.getStartDate() != null && reqDto.getEndDate() != null) {
            prompt += "\nin date range: " + getTimestampForSql(reqDto.getStartDate()) + " to " + getTimestampForSql(reqDto.getEndDate());
        }
        prompt += "\nfor tables schema: " + dbSchema;
        //PromptTemplate promptTemplate = new PromptTemplate(prompt); todo R&D on its usage
        return prompt;
    }

}
