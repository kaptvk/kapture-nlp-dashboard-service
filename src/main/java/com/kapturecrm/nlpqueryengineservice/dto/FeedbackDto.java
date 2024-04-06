package com.kapturecrm.nlpqueryengineservice.dto;

import lombok.Data;

@Data
public class FeedbackDto {
    private int promptId;
    private Boolean isSatisfied;
    private String suggestion;
}
