package com.kapture.nlpdashboardservice.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
@Slf4j
public class ClickHouseDBManager {

    private static String dbUrl;
    private static String user;
    private static String password;

    @Autowired
    public ClickHouseDBManager(@Value("${spring.main.clickhouse.datasource.url}") String _url,
                               @Value("${spring.main.clickhouse.datasource.username}") String _user,
                               @Value("${spring.main.clickhouse.datasource.password}") String _password) {
        dbUrl = _url;
        user = _user;
        password = _password;
    }

    public static Connection getConnection() {
        try {
//            Properties props = new Properties();
//            props.setProperty(ClickHouseClientOption.RENAME_RESPONSE_COLUMN.getKey(),ClickHouseRenameMethod.TO_CAMELCASE_WITHOUT_PREFIX.name());
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error clickhouse getConnection: ", e);
            throw new RuntimeException("Clickhouse Connection Error");
        }
    }

    public static void closeConn(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            log.error("Error in clickhouse closeConn", e);
        }
    }
}
