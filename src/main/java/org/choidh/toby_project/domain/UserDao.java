package org.choidh.toby_project.domain;

import org.choidh.toby_project.connection.ConnectionMaker;
import org.choidh.toby_project.connection.SimpleConnectionMaker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao() {
//        this.connectionMaker = new SimpleConnectionMaker();
    }

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws Exception {
        Connection conn = this.connectionMaker.makeConnection();

        PreparedStatement ps = conn.prepareStatement(
                "insert into user(id, name, password) values(?, ?, ?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        int result = ps.executeUpdate();

        if( result == 1 ) {
            ps.close();
            conn.close();
        }else {
            throw new RuntimeException("insert Error");
        }
    }

    public User get(String id) throws Exception{
        Connection conn = this.connectionMaker.makeConnection();

        PreparedStatement ps = conn.prepareStatement(
                "select * from user where id = ?"
        );
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        conn.close();

        return user;
    }
}
