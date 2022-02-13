package org.choidh.toby_project.domain;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.statement.AddStatement;
import org.choidh.toby_project.statement.CountStatement;
import org.choidh.toby_project.statement.DeleteStatement;
import org.choidh.toby_project.statement.Statement;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@NoArgsConstructor
public class UserDao {

    private JdbcContext context;

    public UserDao(JdbcContext context) {
        this.context = context;
    }

    public void deleteAll() {
        context.jdbcContextWithStatementStrategy(new DeleteStatement());
    }

    public void deleteAllAnnony() {
        context.jdbcContextWithStatementStrategy(conn -> conn.prepareStatement("delete from user"));
    }

    // 전략 패턴으로 인해 클래스가 많아지는 현상을 줄이는 방법중 하나는 내부 클래스를 선언하는 것
    public void addWithInnerClass(final User user) {
        class AddStatement implements Statement{
            @Override
            public PreparedStatement getStatement(Connection conn) throws SQLException {
                final PreparedStatement ps = conn.prepareStatement("insert into user(id, name, password) values(?, ?, ?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        }

        context.jdbcContextWithStatementStrategy(new AddStatement());
    }

    // 익명 클래스 사용
    public void addWithAnonyClass(final User user) {
        context.jdbcContextWithStatementStrategy(conn -> {
            final PreparedStatement ps = conn.prepareStatement("insert into user(id, name, password) values(?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
    }

    public void add(User user) {
        context.jdbcContextWithStatementStrategy(new AddStatement(user));
    }

    public User get(String id) throws DataAccessException {
        try (
                Connection conn = con.dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select * from user where id = ?")
        ) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return User.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .password(rs.getString("password"))
                            .build();
                }
            } catch (SQLException err) {
                log.error(err.getMessage());
            }
        } catch (SQLException err) {
            log.error(err.getMessage());
        }

        throw new EmptyResultDataAccessException(1);
    }

    public int getCount() {
        return context.jdbcContextWithStatementStrategy(new CountStatement());
    }

    // method로 분리 or Statement처럼 전략패턴 사용
    /*private PreparedStatement getStatement(Connection conn, String query) throws SQLException {
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        return ps;
    }*/
}
