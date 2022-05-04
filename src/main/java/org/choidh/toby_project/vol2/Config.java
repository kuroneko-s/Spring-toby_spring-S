package org.choidh.toby_project.vol2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Bean
    @Qualifier("class")
    public FamilyClass familyClass(){
        return new ParentClass();
    }

    @Bean
    public InnerClass innerClass(FamilyClass familyClass) {
        return new InnerClass(familyClass);
    }

}
