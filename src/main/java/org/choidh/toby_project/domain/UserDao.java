package org.choidh.toby_project.domain;

import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteAll() {
        try(
                Connection connection = this.dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("delete from user")
        ) {
            ps.executeUpdate();
        } catch(SQLException err){
//            throw new DataAccessException(err.getMessage());
            log.error(err.getMessage());
        }
    }

    public void deleteAllAnnony() {
        this.jdbcContextWithStatementStrategy(conn -> conn.prepareStatement("delete from user"));
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

        this.jdbcContextWithStatementStrategy(new AddStatement());
    }
    // 익명 클래스 사용
    public void addWithAnonyClass(final User user) {
        this.jdbcContextWithStatementStrategy(conn -> {
            final PreparedStatement ps = conn.prepareStatement("insert into user(id, name, password) values(?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
    }

    public void add(User user) {
        try (
                final Connection conn = this.dataSource.getConnection();
                final PreparedStatement ps = conn.prepareStatement("insert into user(id, name, password) values(?, ?, ?)");
        ) {

            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException err) {
            log.error(err.getMessage());
        }
    }

    public User get(String id) throws SQLException {
        Connection conn = this.dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "select * from user where id = ?"
        );
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        User user = null;
        if(rs.next()){
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        conn.close();

        if(user == null ) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public int getCount() {
        int result = -1;
        try(
                Connection conn = this.dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select count(*) from user");
                ResultSet rs = ps.executeQuery()
        ) {
            rs.next();
            result = rs.getInt(1);
        }catch(SQLException err) {
            log.error(err.getMessage());
        }

        return result;
    }

    // 전략 패턴
    public int jdbcContextWithStatementStrategy(Statement st) {
        int result = 0;
        try (
                final Connection conn = dataSource.getConnection();
                final PreparedStatement ps = st.getStatement(conn);
        ) {
            if (st instanceof CountStatement){
                try (ResultSet  rs = ps.executeQuery() ) {
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

    // method로 분리 or Statement처럼 전략패턴 사용
    private PreparedStatement getStatement(Connection conn, String query) throws SQLException {
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        return ps;
    }
}
