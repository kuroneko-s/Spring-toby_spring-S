package org.choidh.toby_project;

import org.choidh.toby_project.domain.User;
import org.choidh.toby_project.domain.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AppTests {
    // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    // GenericXmlApplicationContext context = new GenericXmlApplicationContext("classpath:application-context.xml");
    // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml", "ExamplePath.class);
    ApplicationContext context = new GenericXmlApplicationContext("classpath:application-context.xml");

    @BeforeEach
    public void setup() throws Exception{
        UserDao userDao = context.getBean("userDao", UserDao.class);
        userDao.deleteAll();
    }

    @Test
    @DisplayName("getCount() 검증")
    void validGetCount() throws Exception{
        UserDao userDao = context.getBean("userDao", UserDao.class);
        int result = userDao.getCount();
        assertEquals(result, 0);

        User user_1 = new User("1", "choi", "test1");
        userDao.add(user_1);
        result = userDao.getCount();
        assertEquals(result, 1);

        User user_2 = new User("2", "dong", "test2");
        userDao.add(user_2);
        result = userDao.getCount();
        assertEquals(result, 2);

        User user_3 = new User("3", "hyuk", "test3");
        userDao.add(user_3);
        result = userDao.getCount();
        assertEquals(result, 3);
    }

    @Test
    @DisplayName("더하고 가져오고")
    void addAndGet() throws SQLException {
        User user_1 = new User("springex1", "CHOI", "1234");
        User user_2 = new User("springex2", "DONGHYUK", "4321");

        UserDao userDAO = context.getBean("userDao", UserDao.class);
        assertEquals(userDAO.getCount(), 0);

        userDAO.add(user_1);
        userDAO.add(user_2);

        int after = userDAO.getCount();
        assertEquals(after, 2);

        User newUser = userDAO.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());

        assertEquals(newUser.getPassword(), user_1.getPassword());
        assertNotEquals(newUser.getPassword(), user_2.getPassword());
    }

    @Test
    @DisplayName("더하고 가져오고 ( 에러 발생 )")
    void addAndGet_Exception() throws SQLException {
        User user_1 = new User("springex1", "CHOI", "1234");
        User user_2 = new User("springex2", "DONGHYUK", "4321");

        UserDao userDAO = context.getBean("userDao", UserDao.class);
        assertEquals(userDAO.getCount(), 0);

        userDAO.add(user_1);
        userDAO.add(user_2);

        int after = userDAO.getCount();
        assertEquals(after, 2);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDAO.get("unknown_id");
        });

        User newUser = userDAO.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());
    }

}
