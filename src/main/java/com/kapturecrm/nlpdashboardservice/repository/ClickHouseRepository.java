package com.kapturecrm.nlpdashboardservice.repository;


import com.kapturecrm.nlpdashboardservice.cache.TableNameToSchemaCache;
import com.kapturecrm.nlpdashboardservice.component.ClickHouseDBManager;
import com.kapturecrm.nlpdashboardservice.exception.KaptureException;
import com.kapturecrm.nlpdashboardservice.utility.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClickHouseRepository {

    private final TableNameToSchemaCache tableNameToSchemaCache;

    public List<LinkedHashMap<String, Object>> findListOfDataFromSql(String sql) throws KaptureException {
        List<LinkedHashMap<String, Object>> resp = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ClickHouseDBManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
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
            log.error("Error in findListOfDataFromSql", e);
            throw new KaptureException(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "AI generated incorrect SQL"));
        } finally {
            ClickHouseDBManager.closeConn(conn);
        }
        return resp;
    }

    public void findDBTableSchema(String tableName, JSONObject dbSchema) {
        String tableSchema = tableNameToSchemaCache.get(tableName);
        if (tableSchema != null) {
            dbSchema.put(tableName, tableSchema);
            return;
        }
        Connection conn = null;
        try {
            conn = ClickHouseDBManager.getConnection();
            String dbName = conn.getCatalog();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(dbName, null, tableName, null);
            JSONObject schema = new JSONObject();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");
                String comment = rs.getString("REMARKS");
                schema.put(columnName, StringUtils.isEmpty(comment) ? ("Type:" + columnType) : ("Type:" + columnType + ", Remarks:" + comment));
            }
            dbSchema.put(tableName, schema);
            try {
                new Thread(() -> tableNameToSchemaCache.put(tableName, schema.toString())).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in findDBTableSchema", e);
        } finally {
            ClickHouseDBManager.closeConn(conn);
        }
    }

    @Deprecated
    public void findTableSchemaByNamePattern(String tableNameKey, JSONObject schema) {
        Connection conn = null;
        try {
            conn = ClickHouseDBManager.getConnection();
            String sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name LIKE '%" + tableNameKey + "%';";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                String columnName = rs.getString("column_name");
                String columnType = rs.getString("data_type");
                String comment = rs.getString("comment");
                JSONObject tableSchema = schema.getJSONObject(tableName);
                if (tableSchema == null || tableSchema.isNullObject()) {
                    tableSchema = new JSONObject();
                    schema.put(tableName, tableSchema);
                }
                tableSchema.put(columnName, "Type: " + columnType + ", Comment: " + comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in findTableSchema", e);
        } finally {
            ClickHouseDBManager.closeConn(conn);
        }
    }

    public void findAllTableNames(HashSet<String> chTableNames) {
        Connection conn = null;
        try {
            conn = ClickHouseDBManager.getConnection();
            String query = "SELECT name FROM system.tables WHERE database IN ('kapture')";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("name");
                chTableNames.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in findTableNames", e);
        } finally {
            ClickHouseDBManager.closeConn(conn);
        }
    }

}

