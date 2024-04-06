package com.kapturecrm.nlpqueryengineservice.dto;


import lombok.Data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class NlpDashboardResponse {
    private int promptId;
    private String dashboardType;
    private Collection<String> dashboardColumns;
    private List<LinkedHashMap<String, Object>> dashboardValues;
    private String textResponse;
}
