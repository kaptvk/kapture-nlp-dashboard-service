package com.kapturecrm.nlpqueryengineservice.utility;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NlpDashboardUtils {

    private static final Map<String, String> nameConversionMap = new HashMap<>();

    static {
        nameConversionMap.put("customer", "cm_lead_member");
        nameConversionMap.put("product", "cm_product");
        nameConversionMap.put("employee", "cm_employee");
        nameConversionMap.put("order", "cm_lead_confirmation_detail");
        nameConversionMap.put("ticket", "cm_general_task");
        nameConversionMap.put("queue", "task_queue_type");
        nameConversionMap.put("folder", "task_folder");
    }

    public record PromptInfo(String prompt, List<String> tableNames) {
    }

    public static PromptInfo convertTableName(String prompt) {
        List<String> tableNames = new ArrayList<>();
        if (StringUtils.isNotBlank(prompt)) {
            prompt = prompt.toLowerCase();
            for (String key : nameConversionMap.keySet()) {
                if (prompt.contains(key)) {
                    String tableName = nameConversionMap.get(key);
                    tableNames.add(tableName);
                    prompt = prompt.replace(key, tableName);
                }
            }
        }
        return new PromptInfo(prompt, tableNames);
    }

}
