package com.kapturecrm.nlpqueryengineservice.repository;

import com.google.gson.JsonObject;
import com.kapturecrm.nlpqueryengineservice.utility.ClickHouseConnUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardRepository {

    public List<JsonObject> findNlpDashboardDataFromSql(String sql) {
        List<JsonObject> resp = new LinkedList<>();
        Connection conn = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            final ResultSetMetaData meta = rs.getMetaData();
            final int columnCount = meta.getColumnCount();
            final List<String> columnNames = new LinkedList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(meta.getColumnName(i));
            }
            while (rs.next()) {
                JsonObject rowObj = new JsonObject();
                for (int idx = 1; idx <= columnCount; ++idx) {
                    final Object value = rs.getObject(idx);
                    rowObj.addProperty(columnNames.get(idx-1), String.valueOf(value));
                }
                resp.add(rowObj);
            }
        } catch (Exception e) {
            log.error("Error in findNlpDashboardDataFromSql", e);
        } finally {
            ClickHouseConnUtil.closeConn(conn);
        }
        return resp;
    }

    public JsonObject getDatabaseSchema(List<String> tableList) {
        JsonObject tableSchema = new JsonObject();
        Connection conn = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            if (conn != null) {
                String dbName = conn.getCatalog();
                DatabaseMetaData metaData = conn.getMetaData();
                for (String tableName : tableList) {
                    ResultSet rs = metaData.getColumns(dbName, null, tableName, null);
                    JsonObject schema = new JsonObject();
                    while (rs.next()) {
                        String columnName = rs.getString("COLUMN_NAME");
                        String columnType = rs.getString("TYPE_NAME");
                        schema.addProperty(columnName, columnType);
                    }
                    tableSchema.add(tableName, schema);
                }
            }
        } catch (Exception e) {
            log.error("Error in getDatabaseSchema", e);
        } finally {
            if (conn != null) {
                ClickHouseConnUtil.closeConn(conn);
            }
        }
        return tableSchema;
    }

}

