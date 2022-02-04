package org.choidh.toby_project;

public class CountingConnectionMaker implements ConnectionMaker{

    private int count = 0;
    ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    @Override
    public void createConnection() {
        this.count += 1;
        this.realConnectionMaker.createConnection();
    }

    public int getCount() {
        return count;
    }
}
