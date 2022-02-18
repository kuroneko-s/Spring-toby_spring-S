package org.choidh.toby_project.domain;

import java.sql.Connection;
import java.sql.DriverManager;

@Deprecated
public class DUserDaoImpl extends UserDaoJdbc {
    // abstract to another class method
    protected Connection getConnection() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/toby", "sa", "");
        return conn;
    }
}
