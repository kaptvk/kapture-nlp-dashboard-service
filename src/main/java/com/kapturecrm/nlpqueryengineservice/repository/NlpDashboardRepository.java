package com.kapturecrm.nlpqueryengineservice.repository;


import com.kapturecrm.nlpqueryengineservice.cache.TableNameToSchemaCache;
import com.kapturecrm.nlpqueryengineservice.utility.ClickHouseConnUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardRepository {

    private final TableNameToSchemaCache tableNameToSchemaCache;

    public List<LinkedHashMap<String, Object>> findNlpDashboardDataFromSql(String sql) {
        List<LinkedHashMap<String, Object>> resp = new ArrayList<>();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
        } catch (Exception e) {
            log.error("Error in findNlpDashboardDataFromSql", e);
        } finally {
            ClickHouseConnUtil.closeConn(conn);
        }
        try {
            if (rs != null) {
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
            }
        } catch (Exception e) {
            log.error("Error in findNlpDashboardDataFromSql process rs: ", e);
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
        ResultSet rs = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            String dbName = conn.getCatalog();
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getColumns(dbName, null, tableName, null);
        } catch (Exception e) {
            log.error("Error in getDatabaseSchema", e);
        } finally {
            ClickHouseConnUtil.closeConn(conn);
        }
        try {
            if (rs != null) {
                JSONObject schema = new JSONObject();
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    String columnType = rs.getString("TYPE_NAME");
                    schema.put(columnName, columnType);
                }
                dbSchema.put(tableName, schema);
                tableNameToSchemaCache.put(tableName, schema.toString());
            }
        } catch (Exception e) {
            log.error("Error in getDatabaseSchema", e);
        }
    }

}

