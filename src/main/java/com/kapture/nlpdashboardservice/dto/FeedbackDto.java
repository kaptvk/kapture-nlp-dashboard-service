package com.kapture.nlpdashboardservice.dto;

import lombok.Data;

@Data
public class FeedbackDto {
    private int promptId;
    private Boolean isSatisfied;
    private String feedback;
}
