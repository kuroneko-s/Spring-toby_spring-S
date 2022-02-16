package org.choidh.toby_project.domain;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.statement.AddStatement;
import org.choidh.toby_project.statement.CountStatement;
import org.choidh.toby_project.statement.DeleteStatement;
import org.choidh.toby_project.statement.Statement;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j

public class UserDao {
    private JdbcContext context;

    // jdbcContext를 빈으로 등록하지 않고 의존을 하는 방향을 조금씩 빠군 방법
    // UserDao - JdbcContext - DataSource 이렇게 참조를 하곤 있는데
    // 실질적으로는 UserDao가 DataSource를 DI 받아서 참조를 넣어주고 있음
    public void setDataSource(DataSource dataSource) {
        this.context = new JdbcContext();
        context.setDataSource(dataSource);
    }

    public void deleteAll() {
        context.jdbcContextWithStatementStrategy(new DeleteStatement());
    }

    public void deleteAllAnnony() {
        this.context.executeSql("delete from user");
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
        context.executeSql("insert into user(id, name, password) values(?, ?, ?)"
                        , user.getId(), user.getName(), user.getPassword());
    }

    public void add(User user) {
        context.jdbcContextWithStatementStrategy(new AddStatement(user));
    }

    public User get(String id) throws DataAccessException {
        return this.context.getJdbcContextWithStatementStrategy(conn -> {
            final PreparedStatement ps = conn.prepareStatement("select * from user where id = ?");
            ps.setString(1, id);
            return ps;
        });
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
