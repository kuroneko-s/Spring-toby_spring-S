package org.choidh.toby_project;

public class UserDAO {

    ConnectionMaker connectionMaker;

    public UserDAO(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void createConnection() {
        this.connectionMaker.createConnection();
    }
}
