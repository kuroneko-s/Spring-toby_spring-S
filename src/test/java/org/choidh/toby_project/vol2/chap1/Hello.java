package org.choidh.toby_project.vol2.chap1;

public class Hello {

    private String name = "default";
    private Printer printer;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void println(){
        System.out.println("Hello " + name);
    }

    public void printWithName() {
        this.printer.println("Hi", this.name);
    }


}
