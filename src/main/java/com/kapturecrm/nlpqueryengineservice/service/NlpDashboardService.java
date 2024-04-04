package com.kapturecrm.nlpqueryengineservice.service;


import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardReqDto;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NlpDashboardService {

    @Value("${openai.apiKey}")
    private String apiKey;

    public ResponseEntity<?> generateNlpDashboard(NlpDashboardReqDto reqDto) {
        String prompt = reqDto.getPrompt();
        // replace entity name with actual table name
        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String aiReply = model.generate(prompt);
        // extract correct sql from aiReply
        String sql = aiReply;
        List<Object> dashboardResponse = null;
        return ResponseEntity.ok(dashboardResponse);
    }

}
