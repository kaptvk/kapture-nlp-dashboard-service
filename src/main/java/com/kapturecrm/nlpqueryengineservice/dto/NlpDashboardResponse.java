package com.kapturecrm.nlpqueryengineservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class NlpDashboardResponse {
    private String dashboardType;
    private List<Object> dashboardValues;
}
