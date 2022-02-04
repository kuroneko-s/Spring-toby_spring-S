package org.choidh.toby_project;

@SampleAnnotation
public class CConnectionMaker implements ConnectionMaker{
    @Override
    public void createConnection() {
        System.out.println("CConnectionMaker");
    }
}
