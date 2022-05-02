package org.choidh.toby_project.vol2.chap1;

public class StringPrinter implements Printer{
    @Override
    public void println(String str, String name) {
        System.out.println(str + " " + name);
    }
}
