package com.kapturecrm.nlpqueryengineservice.service;

import net.sf.json.JSONObject;
import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardReqDto;
import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardResponse;
import com.kapturecrm.nlpqueryengineservice.repository.NlpDashboardRepository;
import com.kapturecrm.nlpqueryengineservice.utility.NlpDashboardUtils;
import com.kapturecrm.session.SessionManager;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NlpDashboardService {

    @Value("${openai.apiKey}")
    private String apiKey;

    private final NlpDashboardRepository nlpDashboardRepository;
    private final HttpServletRequest httpServletRequest;

    public ResponseEntity<?> generateNlpDashboard(NlpDashboardReqDto reqDto) {
        String prompt = reqDto.getPrompt();
        prompt = getPromptForAI(reqDto.getPrompt());

        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String aiReply = model.generate(prompt);

        String sql = validateAIGeneratedSQL(aiReply);

        NlpDashboardResponse resp = new NlpDashboardResponse();
        resp.setDashboardValues(nlpDashboardRepository.findNlpDashboardDataFromSql(sql));
        resp.setDashboardType("table");
        return ResponseEntity.ok(resp);
    }

    private String validateAIGeneratedSQL(String aiReply) {
        String sql = aiReply.replace("[\n\r]", " ")
                .replaceAll("[;]", "");
        int cmId = SessionManager.getPartner(httpServletRequest).getCmId();
        if (!sql.contains("where")) {
            sql += " where cm_id = " + cmId;
        } else if (!sql.split("where")[1].contains("cm_id")) {
            sql = sql.replace("where", "where cm_id = " + cmId + " and ");
        }
        // todo if date where clause is not present then add limit 1000
        return sql;
    }

    private String getPromptForAI(String prompt) {
        try {
            NlpDashboardUtils.PromptInfo promptInfo = NlpDashboardUtils.convertTableName(prompt);
            prompt = promptInfo.prompt();
            JSONObject dbSchema = nlpDashboardRepository.getDatabaseSchema(promptInfo.tableNames());
            prompt = "give clickhouse sql query for " +
                    " prompt :" + prompt +
                    " for tables schema :" + dbSchema.toString() +
                    " ";
            //PromptTemplate promptTemplate = new PromptTemplate(prompt); todo R&D on its usage
        } catch (Exception e) {
            log.error("Error in getCorrectedPrompt", e);
        }
        return prompt;
    }

}
