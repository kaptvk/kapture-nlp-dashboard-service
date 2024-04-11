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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NlpDashboardPromptService {

    private final HttpServletRequest httpServletRequest;
    private final NLPDPromptRepository NLPDPromptRepository;

    public ResponseEntity<?> updateFeedback(FeedbackDto feedbackDto) {
        try {
            if (feedbackDto != null) {
                NLPDPrompt NLPDPrompt = new NLPDPrompt();
                NLPDPrompt.setId(feedbackDto.getPromptId());
                NLPDPrompt.setIsSatisfied(feedbackDto.getIsSatisfied());
                if (!feedbackDto.getIsSatisfied() && feedbackDto.getFeedback() != null) {
                    NLPDPrompt.setFeedback(feedbackDto.getFeedback());
                }
                if (NLPDPromptRepository.addPrompt(NLPDPrompt)) {
                    return BaseResponse.success("Feedback updated successfully.");
                } else {
                    return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update feedback.");
                }
            } else {
                return BaseResponse.error(HttpStatus.BAD_REQUEST, "Feedback data is null.");
            }
        } catch (Exception e) {
            log.error("Error in updateFeedback", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }


    public ResponseEntity<?> getRecentPrompts() {
        try {
            PartnerUser partnerUser = SessionManager.getPartnerUser(httpServletRequest);
            int cmId = partnerUser != null ? partnerUser.getCmId() : 396;
            int empId = partnerUser != null ? partnerUser.getEmpId() : 396;
            List<NLPDPrompt> prompts = NLPDPromptRepository.getRecentPrompts(cmId, empId);
            return BaseResponse.success(prompts);
        } catch (Exception e) {
            log.error("Error in getPrompt", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }


}
