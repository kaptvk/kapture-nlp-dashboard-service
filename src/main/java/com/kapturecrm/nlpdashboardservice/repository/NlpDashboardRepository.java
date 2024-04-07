package com.kapturecrm.nlpdashboardservice.repository;


import com.kapturecrm.nlpdashboardservice.cache.TableNameToSchemaCache;
import com.kapturecrm.nlpdashboardservice.exception.KaptureException;
import com.kapturecrm.nlpdashboardservice.utility.BaseResponse;
import com.kapturecrm.nlpdashboardservice.utility.ClickHouseConnUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardRepository {

    private final TableNameToSchemaCache tableNameToSchemaCache;

    public List<LinkedHashMap<String, Object>> findNlpDashboardDataFromSql(String sql) throws KaptureException {
        List<LinkedHashMap<String, Object>> resp = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            final ResultSetMetaData meta = rs.getMetaData();
            final int columnCount = meta.getColumnCount();
            while (rs.next()) {
                LinkedHashMap<String, Object> rowObj = new LinkedHashMap<>();
                for (int idx = 1; idx <= columnCount; ++idx) {
                    Object value = rs.getObject(idx);
                    rowObj.put(meta.getColumnName(idx), String.valueOf(value));
                }
                resp.add(rowObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in findNlpDashboardDataFromSql", e);
            throw new KaptureException(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "AI generated incorrect SQL"));
        } finally {
            ClickHouseConnUtil.closeConn(conn);
        }
        return resp;
    }

    public void getDatabaseTableSchema(String tableName, JSONObject dbSchema) {
        String tableSchema = tableNameToSchemaCache.get(tableName);
        if (tableSchema != null) {
            dbSchema.put(tableName, tableSchema);
            return;
        }
        Connection conn = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            String dbName = conn.getCatalog();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(dbName, null, tableName, null);
            JSONObject schema = new JSONObject();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");
                schema.put(columnName, columnType);
            }
            dbSchema.put(tableName, schema);
            try {
                new Thread(() -> tableNameToSchemaCache.put(tableName, schema.toString())).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in getDatabaseSchema", e);
        } finally {
            ClickHouseConnUtil.closeConn(conn);
        }
    }

}

