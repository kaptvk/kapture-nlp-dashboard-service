package com.kapturecrm.nlpqueryengineservice.service;

import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardReqDto;
import com.kapturecrm.nlpqueryengineservice.repository.NlpDashboardRepository;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardService {

    @Value("${openai.apiKey}")
    private String apiKey;

    private final NlpDashboardRepository nlpDashboardRepository;

    public ResponseEntity<?> generateNlpDashboard(NlpDashboardReqDto reqDto) {
        String prompt = reqDto.getPrompt();
        //todo replace entity name with actual table name
        //todo get schema
        prompt = getCorrectedPrompt(reqDto.getPrompt());

        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String aiReply = model.generate(prompt);

        //todo extract correct sql from aiReply
        String sql = aiReply;

        List<Object> dashboardResponse = getNlpDashboardData(sql);
        return ResponseEntity.ok(dashboardResponse);
    }

    private String getCorrectedPrompt(String prompt) {
        String template = "";
        PromptTemplate promptTemplate = new PromptTemplate(template);
        return prompt;
    }

    private List<Object> getNlpDashboardData(String sql) {
        //todo
        return nlpDashboardRepository.findNlpDashboardDataFromSql(sql);
    }

}
