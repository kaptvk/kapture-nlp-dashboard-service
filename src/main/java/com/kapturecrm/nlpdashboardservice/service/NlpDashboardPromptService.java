package com.kapturecrm.nlpdashboardservice.service;

import com.kapturecrm.nlpdashboardservice.model.NlpDashboardPrompt;
import com.kapturecrm.nlpdashboardservice.dto.FeedbackDto;
import com.kapturecrm.nlpdashboardservice.repository.MysqlRepo;
import com.kapturecrm.nlpdashboardservice.utility.BaseResponse;
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
    private final BaseResponse baseResponse;
    private final MysqlRepo mysqlRepo;

    public ResponseEntity<?> updateFeedback(FeedbackDto feedbackDto) {
        try {
            if (feedbackDto != null) {
                NlpDashboardPrompt nlpDashboardPrompt = new NlpDashboardPrompt();
                nlpDashboardPrompt.setId(feedbackDto.getPromptId());
                nlpDashboardPrompt.setIsSatisfied(feedbackDto.getIsSatisfied());
                if (!feedbackDto.getIsSatisfied() && feedbackDto.getFeedback() != null) {
                    nlpDashboardPrompt.setFeedback(feedbackDto.getFeedback());
                }
                if (mysqlRepo.addPrompt(nlpDashboardPrompt)) {
                    return baseResponse.successResponse("Feedback updated successfully.");
                } else {
                    return baseResponse.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update feedback.");
                }
            } else {
                return baseResponse.errorResponse(HttpStatus.BAD_REQUEST, "Feedback data is null.");
            }
        } catch (Exception e) {
            log.error("Error in updateFeedback", e);
            return baseResponse.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }


    public ResponseEntity<?> getPrompt() {
        try {
            PartnerUser partnerUser = SessionManager.getPartnerUser(httpServletRequest);
            int cmId = partnerUser != null ? partnerUser.getCmId() : 0;
            int empId = partnerUser != null ? partnerUser.getEmpId() : 0;
            List<NlpDashboardPrompt> nlpDashboardPrompt = mysqlRepo.getPrompt(cmId, empId);
            if (!nlpDashboardPrompt.isEmpty()) {
                return baseResponse.successResponse(nlpDashboardPrompt);
            } else {
                return baseResponse.errorResponse(HttpStatus.NOT_FOUND, "Prompt not found");
            }
        } catch (Exception e) {
            log.error("Error in getPrompt", e);
            return baseResponse.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }


}
