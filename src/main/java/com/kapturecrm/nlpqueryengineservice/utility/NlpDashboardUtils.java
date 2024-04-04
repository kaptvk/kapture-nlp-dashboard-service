package com.kapturecrm.nlpqueryengineservice.utility;

import com.kapturecrm.nlpqueryengineservice.component.StaticContextAccessor;
import com.kapturecrm.nlpqueryengineservice.repository.NlpDashboardRepository;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public static PromptInfo convertTableNameAndFindDBSchema(String prompt, JSONObject dbSchema) {
        NlpDashboardRepository nlpDashboardRepo = StaticContextAccessor.getBean(NlpDashboardRepository.class);
        List<String> tableNames = new ArrayList<>();
        StringBuilder modifiedPrompt = new StringBuilder(prompt);
        if (StringUtils.isNotBlank(prompt)) {
            prompt = prompt.toLowerCase();
            List<Future<?>> futures = new ArrayList<>();
            for (String key : prompt.split(" ")) {
                if (entityNameToTableName.containsKey(key)) {
                    String tableName = entityNameToTableName.get(key);
                    futures.add(threadPool.submit(() -> nlpDashboardRepo.getDatabaseTableSchema(tableName, dbSchema)));
                    tableNames.add(tableName);
                    int index = modifiedPrompt.indexOf(key);
                    modifiedPrompt.replace(index, index + key.length(), tableName);
                }
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
