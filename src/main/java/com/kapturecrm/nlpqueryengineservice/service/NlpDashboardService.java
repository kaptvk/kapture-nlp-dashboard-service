package com.kapturecrm.nlpqueryengineservice.service;

import com.google.gson.JsonObject;
import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardReqDto;
import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardResponse;
import com.kapturecrm.nlpqueryengineservice.repository.NlpDashboardRepository;
import com.kapturecrm.nlpqueryengineservice.utility.NlpDashboardUtils;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NlpDashboardService {

    @Value("${openai.apiKey}")
    private String apiKey;

    private final NlpDashboardRepository nlpDashboardRepository;

    public ResponseEntity<?> generateNlpDashboard(NlpDashboardReqDto reqDto) {
        String prompt = reqDto.getPrompt();
        prompt = getCorrectedPrompt(reqDto.getPrompt());

        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String aiReply = model.generate(prompt);

        //todo extract correct sql from aiReply
        String sql = aiReply;

        NlpDashboardResponse resp = new NlpDashboardResponse();
        resp.setDashboardValues(getNlpDashboardData(sql));
        resp.setDashboardType("table");
        return ResponseEntity.ok(resp);
    }

    private String getCorrectedPrompt(String prompt) {
        try {
            NlpDashboardUtils.PromptInfo promptInfo = NlpDashboardUtils.convertTableName(prompt);
            prompt = promptInfo.prompt();
            JsonObject dbSchema = nlpDashboardRepository.getDatabaseSchema(promptInfo.tableNames());
            prompt = "give clickhouse sql query for " +
                    "prompt :" + prompt + " " +
                    "for tables schema :" + dbSchema.toString();
            PromptTemplate promptTemplate = new PromptTemplate(prompt);
        } catch (Exception e) {
            log.error("Error in getCorrectedPrompt", e);
        }
        return prompt;
    }

    private List<Object> getNlpDashboardData(String sql) {
        //todo
        nlpDashboardRepository.findNlpDashboardDataFromSql(sql);
        return null;
    }

}
