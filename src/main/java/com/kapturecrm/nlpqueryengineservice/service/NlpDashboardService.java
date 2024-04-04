package com.kapturecrm.nlpqueryengineservice.service;

import com.kapturecrm.object.PartnerUser;
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
        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String aiReply = model.generate(getPromptForAI(reqDto.getPrompt()));

        String sql = validateAIGeneratedSQL(aiReply);

        NlpDashboardResponse resp = new NlpDashboardResponse();
        resp.setDashboardValues(nlpDashboardRepository.findNlpDashboardDataFromSql(sql));
        resp.setDashboardType("table");
        return ResponseEntity.ok(resp);
    }

    private String validateAIGeneratedSQL(String aiReply) {
        // todo validate reply if it has only sql its fine, else filter out sql alone check for ``` or ```sql
        String sql = aiReply.replace("[\n\r]", " ")
                .replaceAll("[;]", "");
        PartnerUser partnerUser = SessionManager.getPartnerUser(httpServletRequest);
        int cmId = partnerUser != null ? partnerUser.getCmId() : 0;
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
            JSONObject dbSchema = new JSONObject();
            NlpDashboardUtils.PromptInfo promptInfo = NlpDashboardUtils.convertTableNameAndFindDBSchema(prompt, dbSchema);
            prompt = "give clickhouse sql query for " +
                    " prompt :" + promptInfo.prompt() +
                    " for tables schema :" + dbSchema.toString();
            //PromptTemplate promptTemplate = new PromptTemplate(prompt); todo R&D on its usage
        } catch (Exception e) {
            log.error("Error in getCorrectedPrompt", e);
        }
        return prompt;
    }

}
