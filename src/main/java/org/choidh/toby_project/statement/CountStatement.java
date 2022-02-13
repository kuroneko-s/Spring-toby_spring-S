package org.choidh.toby_project.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CountStatement implements Statement{
    public PreparedStatement getStatement(Connection conn) throws SQLException {
        PreparedStatement ps;
        ps = conn.prepareStatement("select count(*) from user");
        return ps;
    }
}
