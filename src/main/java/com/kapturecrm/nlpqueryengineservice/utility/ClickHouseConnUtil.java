package com.kapturecrm.nlpqueryengineservice.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class ClickHouseConnUtil {

    private static final Logger logger = LoggerFactory.getLogger(ClickHouseConnUtil.class);

    private static String dbUrl;
    private static String user;
    private static String password;

    @Autowired
    public ClickHouseConnUtil(@Value("${spring.main.clickhouse.datasource.url}") String _url,
                              @Value("${spring.main.clickhouse.datasource.username}") String _user,
                              @Value("${spring.main.clickhouse.datasource.password}") String _password) {
        dbUrl = _url;
        user = _user;
        password = _password;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
            logger.error("Error clickhouse getConnection: ", e);
        }
        return null;
    }

    public static void closeConn(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            logger.error("Error in clickhouse closeConn", e);
        }
    }
}
