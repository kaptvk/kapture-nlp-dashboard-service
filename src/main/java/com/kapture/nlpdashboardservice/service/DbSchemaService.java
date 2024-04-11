package com.kapture.nlpdashboardservice.service;

import com.kapture.nlpdashboardservice.cache.TableNameToSchemaCache;
import com.kapture.nlpdashboardservice.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbSchemaService {

    private final TableNameToSchemaCache tableNameToSchemaCache;

    public ResponseEntity<?> clearTableSchemaCache() {
        if (tableNameToSchemaCache.clear()) {
            return BaseResponse.success();
        }
        return BaseResponse.error();
    }
}
