package org.choidh.toby_project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/application-context-test.xml")
class AppTests_2 {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("Context 값 확인_1")
    public void test1() {
        System.out.println(context);
    }

    @Test
    @DisplayName("Context 값 확인_2")
    public void test2() {
        System.out.println(context);
    }

    @Test
    @DisplayName("Context 값 확인_3")
    public void test3() {
        System.out.println(context);
    }
}
