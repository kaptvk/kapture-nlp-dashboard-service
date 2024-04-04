package com.kapturecrm.nlpqueryengineservice.dto;


import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class NlpDashboardResponse {
    private String dashboardType;
    private List<LinkedHashMap<String, Object>> dashboardValues;
}
