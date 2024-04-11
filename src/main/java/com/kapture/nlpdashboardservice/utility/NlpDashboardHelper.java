package com.kapture.nlpdashboardservice.utility;

import com.kapture.nlpdashboardservice.repository.ClickHouseRepository;
import com.kapture.nlpdashboardservice.exception.KaptureException;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardHelper {

    private final ClickHouseRepository clickhouseRepository;

    private static HashSet<String> chTableNames = null;

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public JSONObject getRequiredDatabaseSchema(String prompt) throws KaptureException {
        if (chTableNames == null) {
            initializeTableNames();
        }
        JSONObject dbSchema = new JSONObject();
        List<Future<?>> futures = new ArrayList<>();
        int tableCount = 0;
        for (String promptPart : Set.of(prompt.split(" "))) {
            promptPart = promptPart.toLowerCase().trim();
            String tableName;
            // check for singular as well plural for table name
            if (chTableNames.contains(promptPart)) {
                tableName = promptPart;
            } else if (promptPart.endsWith("s") && chTableNames.contains(promptPart.substring(0, promptPart.length() - 1))) {
                tableName = promptPart.substring(0, promptPart.length() - 1);
            } else {
                tableName = null;
            }
            if (tableName != null) {
                futures.add(threadPool.submit(() -> clickhouseRepository.findDBTableSchema(tableName, dbSchema)));
                tableCount++;
            }
        }
        if (tableCount == 0) {
            throw new KaptureException(BaseResponse.error(HttpStatus.UNPROCESSABLE_ENTITY, "Could not process the prompt!"));
        }
        waitForCompletion(futures);
        return dbSchema;
    }

    private void initializeTableNames() {
        chTableNames = new HashSet<>();
        clickhouseRepository.findAllTableNames(chTableNames);
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

    public static String getAliasForChart(String chartType) {
        return switch (chartType) {
            case "barchart", "number" -> "name,value";
            case "donut" -> "type,value";
            default -> "name,type,value";
        };
    }

}
