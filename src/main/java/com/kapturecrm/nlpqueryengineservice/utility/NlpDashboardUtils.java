package com.kapturecrm.nlpqueryengineservice.utility;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NlpDashboardUtils {

    private static final Map<String, String> entityNameToTableName = new HashMap<>();

    static {
        entityNameToTableName.put("customer", "cm_lead_member");
        entityNameToTableName.put("product", "cm_product");
        entityNameToTableName.put("employee", "cm_employee");
        entityNameToTableName.put("order", "cm_lead_confirmation_detail");
        entityNameToTableName.put("ticket", "cm_general_task");
        entityNameToTableName.put("queue", "task_queue_type");
        entityNameToTableName.put("folder", "task_folder");
    }

    public record PromptInfo(String prompt, List<String> tableNames) {
    }

    public static PromptInfo convertTableName(String prompt) {
        List<String> tableNames = new ArrayList<>();
        if (StringUtils.isNotBlank(prompt)) {
            prompt = prompt.toLowerCase();
            for (String key : prompt.split(" ")) {
                if (entityNameToTableName.containsKey(key)) {
                    String tableName = entityNameToTableName.get(key);
                    tableNames.add(tableName);
                    prompt = prompt.replace(key, tableName);
                }
            }
        }
        return new PromptInfo(prompt, tableNames);
    }

}
