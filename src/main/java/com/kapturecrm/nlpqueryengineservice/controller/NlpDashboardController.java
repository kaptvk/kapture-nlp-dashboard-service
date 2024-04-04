package com.kapturecrm.nlpqueryengineservice.controller;

import com.kapturecrm.nlpqueryengineservice.dto.NlpDashboardReqDto;
import com.kapturecrm.nlpqueryengineservice.service.NlpDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nlp-dashboard")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardController {
    private final NlpDashboardService nlpDashboardService;

    @PostMapping("/generate")
    public ResponseEntity<?> getDataFromNlp(@RequestBody NlpDashboardReqDto reqDto) {
        return nlpDashboardService.generateNlpDashboard(reqDto);
    }

}
