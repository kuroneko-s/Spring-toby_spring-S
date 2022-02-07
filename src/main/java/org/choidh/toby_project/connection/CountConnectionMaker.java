package org.choidh.toby_project.connection;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@NoArgsConstructor
public class CountConnectionMaker implements ConnectionMaker{
    private int count = 0;
    private ConnectionMaker connectionMaker;

//    public CountConnectionMaker(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        this.count++;
        return this.connectionMaker.makeConnection();
    }

    public int getCount() {
        return this.count;
    }
}
