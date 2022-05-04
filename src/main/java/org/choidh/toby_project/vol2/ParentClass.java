package org.choidh.toby_project.vol2;

import org.springframework.stereotype.Component;

//@Primary
@Component
class ParentClass implements FamilyClass{
    public static ParentClass of(){
        return new ParentClass();
    }

    @Override
    public void print() {
        System.out.println("Hello");
    }
}
