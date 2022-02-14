package org.choidh.toby_project.domain;

import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.statement.AddStatement;
import org.choidh.toby_project.statement.CountStatement;
import org.choidh.toby_project.statement.DeleteStatement;
import org.choidh.toby_project.statement.Statement;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 전략 패턴
    public int jdbcContextWithStatementStrategy(Statement st) {
        int result = 0;
        try (
                final Connection conn = dataSource.getConnection();
                final PreparedStatement ps = st.getStatement(conn);
        ) {
            if (st instanceof CountStatement){
                try (ResultSet rs = ps.executeQuery() ) {
                    if ( rs.next() ) result = rs.getInt(1);
                }
            } else if (st instanceof DeleteStatement || st instanceof AddStatement){
                ps.executeUpdate();
            }

        } catch (SQLException err) {
            log.error(err.getMessage());
        }

        return result;
    }

    public User getJdbcContextWithStatementStrategy(Statement st) {
        try(
                final Connection conn = this.dataSource.getConnection();
                final PreparedStatement ps = st.getStatement(conn);
                final ResultSet rs = ps.executeQuery();
        ){
            if (rs.next()){
                return User.builder()
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .build();
            }
        }catch (SQLException err) {
            log.error(err.getMessage());
        }

        throw new EmptyResultDataAccessException(1);
    }

}
