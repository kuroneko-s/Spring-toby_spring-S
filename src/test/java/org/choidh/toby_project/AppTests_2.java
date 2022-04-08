package org.choidh.toby_project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class AppTests_2 extends TestConfig{

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
