package com.kapturecrm.nlpqueryengineservice.utils;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NlpDashboardUtils {

    private static final Map<String, String> nameConversionMap = new HashMap<>();

    static {
        nameConversionMap.put("customer", "cm_lead_member");
        nameConversionMap.put("product", "cm_product");
        nameConversionMap.put("employee", "cm_product");
        nameConversionMap.put("order", "cm_order");
        nameConversionMap.put("ticket", "cm_general_task");
        nameConversionMap.put("queue", "task_queue_type");
        nameConversionMap.put("folder", "task_folder");
    }

    public static String convertTableName(String prompt) {
        if (StringUtils.isNotBlank(prompt)) {
            prompt = prompt.toLowerCase();
            for (String key : nameConversionMap.keySet()) {
                prompt = prompt.replace(key, nameConversionMap.get(key));
            }
        }
        return prompt;
    }

}
