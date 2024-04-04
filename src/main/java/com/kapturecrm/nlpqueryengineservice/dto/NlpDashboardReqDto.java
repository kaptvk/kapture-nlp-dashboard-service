package com.kapturecrm.nlpqueryengineservice.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NlpDashboardReqDto {
    private String prompt;
    private String dashboardType;
    private Timestamp startDate;
    private Timestamp endDate;
}