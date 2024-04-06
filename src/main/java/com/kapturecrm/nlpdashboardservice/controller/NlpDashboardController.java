package com.kapturecrm.nlpdashboardservice.controller;

import com.kapturecrm.nlpdashboardservice.dto.FeedbackDto;
import com.kapturecrm.nlpdashboardservice.dto.NlpDashboardReqDto;
import com.kapturecrm.nlpdashboardservice.service.NlpDashboardService;
import com.kapturecrm.nlpdashboardservice.service.NlpDashboardPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/nlp-dashboard")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardController {

    private final NlpDashboardService nlpDashboardService;
    private final NlpDashboardPromptService nlpDashboardPromptService;

    @PostMapping("/generate")
    public ResponseEntity<?> getDataFromNlp(@RequestBody NlpDashboardReqDto reqDto) {
        return nlpDashboardService.generateNlpDashboard(reqDto);
    }

    @PostMapping("/post-feedback")
    public ResponseEntity<?> postFeedback(@RequestBody FeedbackDto feedbackDto) {
        return nlpDashboardPromptService.updateFeedback(feedbackDto);
    }

    @GetMapping("/get-recent-prompts")
    public ResponseEntity<?> getPrompt() {
        return nlpDashboardPromptService.getPrompt();
    }

}
