package com.kapturecrm.nlpqueryengineservice.controller.scheduler;

import com.kapturecrm.nlpqueryengineservice.service.MySqlToClickHouseSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MysqlToClickhouseSyncScheduler {

    private final MySqlToClickHouseSyncService mySqlToClickHouseSyncService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void syncMySQLDataToClickHouse() {
        mySqlToClickHouseSyncService.syncData();
    }

}
