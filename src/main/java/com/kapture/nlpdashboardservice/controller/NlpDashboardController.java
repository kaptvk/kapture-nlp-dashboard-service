package com.kapture.nlpdashboardservice.controller;

import com.kapture.nlpdashboardservice.dto.FeedbackDto;
import com.kapture.nlpdashboardservice.dto.NlpDashboardReqDto;
import com.kapture.nlpdashboardservice.service.DbSchemaService;
import com.kapture.nlpdashboardservice.service.NLPDPromptService;
import com.kapture.nlpdashboardservice.service.NlpDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/nlp-dashboard")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardController {

    private final NlpDashboardService nlpDashboardService;
    private final NLPDPromptService nlpdPromptService;
    private final DbSchemaService dbSchemaService;

    @PostMapping("/generate")
    public ResponseEntity<?> getDataFromNlp(@RequestBody NlpDashboardReqDto reqDto) {
        return nlpDashboardService.generateNlpDashboard(reqDto);
    }

    @PostMapping("/post-feedback")
    public ResponseEntity<?> postFeedback(@RequestBody FeedbackDto feedbackDto) {
        return nlpdPromptService.updateFeedback(feedbackDto);
    }

    @GetMapping("/get-recent-prompts")
    public ResponseEntity<?> getPrompt() {
        return nlpdPromptService.getRecentPrompts();
    }

    @GetMapping("/clear-table-schema-cache")
    public ResponseEntity<?> clearTableSchemaCache() {
        return dbSchemaService.clearTableSchemaCache();
    }

}
