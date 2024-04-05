package com.kapturecrm.nlpqueryengineservice.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class NlpDashboardReqDto {
    @NotNull
    private String prompt;
    private String dashboardType;
    private Timestamp startDate;
    private Timestamp endDate;
}