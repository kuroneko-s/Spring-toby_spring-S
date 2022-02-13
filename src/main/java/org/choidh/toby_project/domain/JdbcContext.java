package org.choidh.toby_project.domain;

import org.choidh.toby_project.statement.AddStatement;
import org.choidh.toby_project.statement.CountStatement;
import org.choidh.toby_project.statement.DeleteStatement;
import org.choidh.toby_project.statement.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcContext {

    // 전략 패턴
    public int jdbcContextWithStatementStrategy(Statement st) {
        int result = 0;
        try (
                final Connection conn = dataSource.getConnection();
                final PreparedStatement ps = st.getStatement(conn);
        ) {
            if (st instanceof CountStatement){
                try (ResultSet rs = ps.executeQuery() ) {
                    if ( rs.next() ) {
                        result = rs.getInt(1);
                    }
                }
            } else if (st instanceof DeleteStatement || st instanceof AddStatement){
                ps.executeUpdate();
            }

        } catch (SQLException err) {
            log.error(err.getMessage());
        }

        return result;
    }

}
