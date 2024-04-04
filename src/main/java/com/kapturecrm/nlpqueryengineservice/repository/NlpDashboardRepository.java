package com.kapturecrm.nlpqueryengineservice.repository;


import com.kapturecrm.nlpqueryengineservice.utility.ClickHouseConnUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardRepository {


    public List<LinkedHashMap<String, Object>> findNlpDashboardDataFromSql(String sql) {
        List<LinkedHashMap<String, Object>> resp = new ArrayList<>();
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
                LinkedHashMap<String, Object> rowObj = new LinkedHashMap<>();
                for (int idx = 0; idx <= columnCount; ++idx) {
                    Object value = rs.getObject(idx);
                    rowObj.put(columnNames.get(idx), String.valueOf(value));
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

    public void getDatabaseSchema(String tableName, JSONObject dbSchema) {
        Connection conn = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            if (conn != null) {
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
            }
        } catch (Exception e) {
            log.error("Error in getDatabaseSchema", e);
        } finally {
            if (conn != null) {
                ClickHouseConnUtil.closeConn(conn);
            }
        }
    }

}

