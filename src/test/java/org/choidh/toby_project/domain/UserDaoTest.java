package org.choidh.toby_project.domain;

import org.choidh.toby_project.DaoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class UserDaoTest {

    @Test
    public void test(){
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDAO", UserDao.class);
        UserDao userDao1 = context.getBean("userDAO", UserDao.class);

        assertEquals(userDao, userDao1);
    }
}