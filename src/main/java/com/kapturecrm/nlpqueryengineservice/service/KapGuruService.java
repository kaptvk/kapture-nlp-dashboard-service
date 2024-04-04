package com.kapturecrm.nlpqueryengineservice.service;


import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KapGuruService {
    @Value("${openai.apiKey}")
    private String apiKey;
    public ResponseEntity<?> getDataFromNlp(String text) {
        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String answer = model.generate(text);
        System.out.println(answer);
        return ResponseEntity.ok(answer);
    }
}
