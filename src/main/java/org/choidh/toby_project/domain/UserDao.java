package org.choidh.toby_project.domain;

import lombok.NoArgsConstructor;
import org.choidh.toby_project.connection.ConnectionMaker;

import javax.sql.DataSource;
import java.sql.*;

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

    public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement("delete from user");
        ps.executeUpdate();

        ps.close();
        connection.close();
    }

    public void add(User user) throws Exception {
        Connection conn = this.dataSource.getConnection();

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
        Connection conn = this.dataSource.getConnection();

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
