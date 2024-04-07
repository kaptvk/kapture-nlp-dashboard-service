package com.kapturecrm.nlpdashboardservice.utility;

import com.kapturecrm.nlpdashboardservice.exception.KaptureException;
import com.kapturecrm.nlpdashboardservice.repository.NlpDashboardRepository;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardUtils {

    private final NlpDashboardRepository nlpDashboardRepo;

    private static final Map<String, String> entityNameToTableName = new HashMap<>();
    private static final Map<String, String> chartTypeToAlias = new HashMap<>();

    static {
        entityNameToTableName.put("customer", "cm_lead_member");
        entityNameToTableName.put("customers", "cm_lead_member");
        entityNameToTableName.put("contact", "cm_contact_details");
        entityNameToTableName.put("contacts", "cm_contact_details");
        entityNameToTableName.put("product", "cm_lead_product");
        entityNameToTableName.put("products", "cm_lead_product");
        entityNameToTableName.put("employee", "cm_employee");
        entityNameToTableName.put("employees", "cm_employee");
        entityNameToTableName.put("order", "cm_lead_confirmation_detail");
        entityNameToTableName.put("orders", "cm_lead_confirmation_detail");
        entityNameToTableName.put("ticket", "cm_general_task");
        entityNameToTableName.put("tickets", "cm_general_task");
        entityNameToTableName.put("queue", "task_queue_type");
        entityNameToTableName.put("queues", "task_queue_type");
        entityNameToTableName.put("folder", "task_folder");
        entityNameToTableName.put("folders", "task_folder");
    }

    static {
        chartTypeToAlias.put("barchart", "name,value");
        chartTypeToAlias.put("number", "name,value");
        chartTypeToAlias.put("donut", "type,value");
        chartTypeToAlias.put("common", "name,type,value");
    }

    public static String getAliasForChart(String chartType) {
        return chartTypeToAlias.getOrDefault(chartType, chartTypeToAlias.get("common"));
    }

    public record PromptInfo(String prompt, List<String> tableNames) {
    }

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public PromptInfo convertTableNameAndFindDBSchema(String prompt, JSONObject dbSchema) throws KaptureException {
        List<String> tableNames = new ArrayList<>();
        StringBuilder modifiedPrompt = new StringBuilder(prompt);
        if (StringUtils.isNotBlank(prompt)) {
            prompt = prompt.toLowerCase();
            List<Future<?>> futures = new ArrayList<>();
            int tableCount = 0;
            for (String key : prompt.split(" ")) {
                if (entityNameToTableName.containsKey(key)) {
                    tableCount++;
                    String tableName = entityNameToTableName.get(key);
                    if ("cm_lead_member,cm_lead_product,cm_employee".contains(tableName)) {
                        futures.add(threadPool.submit(() -> nlpDashboardRepo.getDatabaseTableSchema("cm_additional_fields_mapping", dbSchema)));
                        futures.add(threadPool.submit(() -> nlpDashboardRepo.getDatabaseTableSchema("cm_additional_fields", dbSchema)));
                    }
                    futures.add(threadPool.submit(() -> nlpDashboardRepo.getDatabaseTableSchema(tableName, dbSchema)));
                    tableNames.add(tableName);
                    int index = prompt.indexOf(key);
                    modifiedPrompt.replace(index, index + key.length(), tableName);
                }
            }
            if (tableCount == 0) {
                throw new KaptureException(BaseResponse.error(HttpStatus.UNPROCESSABLE_ENTITY, "Could not process the prompt!"));
            }
            waitForCompletion(futures);
        }
        return new PromptInfo(modifiedPrompt.toString(), tableNames);
    }

    private static void waitForCompletion(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
