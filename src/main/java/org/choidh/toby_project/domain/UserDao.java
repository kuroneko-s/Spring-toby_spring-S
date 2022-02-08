package org.choidh.toby_project.domain;

import lombok.NoArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class UserDao {

    private DataSource dataSource;

//    public UserDao() {
//        this.connectionMaker = new SimpleConnectionMaker();
//    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

//    public UserDao(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }

    public void deleteAll() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement("delete from user");
        ps.executeUpdate();

        ps.close();
        connection.close();
    }

    public int add(User user) throws SQLException {
        Connection conn = this.dataSource.getConnection();
        int result = -1;

        PreparedStatement ps = conn.prepareStatement(
                "insert into user(id, name, password) values(?, ?, ?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        result = ps.executeUpdate();

        if( result == 1 ) {
            ps.close();
            conn.close();
        }else {
            throw new RuntimeException("insert Error");
        }
        return result;
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

    public int getCount() throws SQLException {
        Connection conn = this.dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select count(*) from user");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int result = rs.getInt(1);

        rs.close();
        ps.close();
        conn.close();

        return result;
    }
}
