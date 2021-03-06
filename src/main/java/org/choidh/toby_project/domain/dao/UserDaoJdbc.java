package org.choidh.toby_project.domain.dao;

import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.domain.Level;
import org.choidh.toby_project.domain.dao.sql.SqlService;
import org.choidh.toby_project.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository(value = "userDao")
public class UserDaoJdbc implements UserDao{
    @Autowired
    private SqlService sqlService;
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(rs.getString("id")
            , rs.getString("name"), rs.getString("password")
            , Level.valueOf(rs.getInt("level")), rs.getInt("login")
            , rs.getInt("recommend"), rs.getString("email"));

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    /*
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

        jdbcTemplate.jdbcContextWithStatementStrategy(new AddStatement());
    }

    // 익명 클래스 사용
    public void addWithAnonyClass(final User user) {
        jdbcTemplate.executeSql("insert into user(id, name, password) values(?, ?, ?)"
                        , user.getId(), user.getName(), user.getPassword());
    }
    */

    @Override
    public void add(User user) throws DuplicateKeyException {
        this.jdbcTemplate
                .update( this.sqlService.getSql("add")
                        , user.getId(), user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }

    @Override
    public User get(String id) throws DataAccessException {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("get"), this.userRowMapper, id);
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("getCount"), Integer.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                this.sqlService.getSql("update"),
                user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend(),user.getEmail(), user.getId()
        );
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(this.sqlService.getSql("getAll"), this.userRowMapper);
    }

    @Override
    public void deleteAll() {
        this.jdbcTemplate.update(this.sqlService.getSql("deleteAll"));
    }

    // method로 분리 or Statement처럼 전략패턴 사용
    /*private PreparedStatement getStatement(Connection conn, String query) throws SQLException {
        PreparedStatement ps;
        ps = conn.prepareStatement(query);
        return ps;
    }*/
}
