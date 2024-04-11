package com.kapture.nlpdashboardservice.service;

import com.kapture.nlpdashboardservice.dto.FeedbackDto;
import com.kapture.nlpdashboardservice.model.NLPDPrompt;
import com.kapture.nlpdashboardservice.repository.mysql.NLPDPromptRepository;
import com.kapture.nlpdashboardservice.utility.BaseResponse;
import com.kapturecrm.object.PartnerUser;
import com.kapturecrm.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NLPDPromptService {

    private final HttpServletRequest httpServletRequest;
    private final NLPDPromptRepository nlpdPromptRepository;

    public ResponseEntity<?> updateFeedback(FeedbackDto feedbackDto) {
        try {
            NLPDPrompt NLPDPrompt = new NLPDPrompt();
            NLPDPrompt.setId(feedbackDto.getPromptId());
            NLPDPrompt.setIsSatisfied(feedbackDto.getIsSatisfied());
            if (!feedbackDto.getIsSatisfied() && feedbackDto.getFeedback() != null) {
                NLPDPrompt.setFeedback(feedbackDto.getFeedback());
            }
            nlpdPromptRepository.save(NLPDPrompt);
            return BaseResponse.success("Feedback updated successfully!");
        } catch (Exception e) {
            log.error("Error in updateFeedback", e);
            return BaseResponse.error(e);
        }
    }

    public ResponseEntity<?> getRecentPrompts() {
        try {
            PartnerUser partnerUser = SessionManager.getPartnerUser(httpServletRequest);
            int cmId = partnerUser != null ? partnerUser.getCmId() : 396;
            int empId = partnerUser != null ? partnerUser.getEmpId() : 396;
            List<NLPDPrompt> prompts = nlpdPromptRepository.findAllByCmIdAndEmpIdOrderByCreateTimeDesc(cmId, empId, PageRequest.of(0, 5));
            return BaseResponse.success(prompts);
        } catch (Exception e) {
            log.error("Error in getPrompt", e);
            return BaseResponse.error(e);
        }
    }


}
