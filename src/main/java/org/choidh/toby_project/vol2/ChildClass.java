package org.choidh.toby_project.vol2;


class ChildClass implements FamilyClass{
    public static ChildClass of() {
        return new ChildClass();
    }

    @Override
    public void print() {
        System.out.println("Hello child");
    }
}
