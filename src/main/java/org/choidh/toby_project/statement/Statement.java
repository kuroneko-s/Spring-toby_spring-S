package org.choidh.toby_project.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 전략패턴 (strategy pattern)
public interface Statement {
    public PreparedStatement getStatement(Connection conn) throws SQLException;
}
