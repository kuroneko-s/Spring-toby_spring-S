package org.choidh.toby_project.vol2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ComponentConfig.class)
public class ComponentConfig {

    @Bean
    public ChildClass childClass(){
        return new ChildClass();
    }

}
