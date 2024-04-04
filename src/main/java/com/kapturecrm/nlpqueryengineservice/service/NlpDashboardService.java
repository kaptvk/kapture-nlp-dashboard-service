package com.kapturecrm.nlpqueryengineservice.service;


import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardReqDto;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NlpDashboardService {

    @Value("${openai.apiKey}")
    private String apiKey;

    public ResponseEntity<?> generateNlpDashboard(NlpDashboardReqDto reqDto) {
        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String answer = model.generate(reqDto.getPrompt());
        System.out.println(answer);
        return ResponseEntity.ok(answer);
    }

}
