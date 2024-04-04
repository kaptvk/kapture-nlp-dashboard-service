package com.kapturecrm.nlpqueryengineservice.dto;

import com.google.gson.JsonObject;
import lombok.Data;

import java.util.List;

@Data
public class NlpDashboardResponse {
    private String dashboardType;
    private List<JsonObject> dashboardValues;
}
