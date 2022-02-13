package org.choidh.toby_project.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteStatement implements Statement{
    @Override
    public PreparedStatement getStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("delete from user");
    }
}
