package com.kapturecrm.nlpqueryengineservice.repository;

import com.kapturecrm.nlpqueryengineservice.utility.ClickHouseConnUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NlpDashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Object> findNlpDashboardDataFromSql(String sql) {
        List<Object> resp = null;
        Connection conn = null;
        try {
            conn = ClickHouseConnUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            // todo
        } catch (Exception e) {
            log.error("Error in findNlpDashboardDataFromSql", e);
        } finally {
            ClickHouseConnUtil.closeConn(conn);
        }
        return resp;
    }
}
