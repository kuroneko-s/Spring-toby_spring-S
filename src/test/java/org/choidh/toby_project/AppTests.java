package org.choidh.toby_project;

import org.choidh.toby_project.domain.User;
import org.choidh.toby_project.domain.UserDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/application-context-test.xml")
//@DirtiesContext
class AppTests {
    // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    // GenericXmlApplicationContext context = new GenericXmlApplicationContext("classpath:application-context-test.xml");
    // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context-test.xml", "ExamplePath.class);
    @Autowired private UserDao userDao;
    private static User user_1;
    private static User user_2;
    private static User user_3;

    @BeforeAll
    public static void beforeAll() {
        user_1 = new User("springex1", "CHOI", "1234");
        user_2 = new User("springex2", "DONGHYUK", "4321");
        user_3 = new User("springex3", "CHOIDONGHYUK", "9876");
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        // 아래처럼 쓰면 테스트 코드 속도가 엄청빨라짐
        // DataSource dataSource = new SingleConnectionDataSource("jdbc:h2:~/toby", "sa", "", true);
        // this.userDao.setDataSource(dataSource);
        userDao.deleteAll();
    }

    @Test
    @DisplayName("getCount() 검증")
    void validGetCount() throws Exception {
        int result = userDao.getCount();
        assertEquals(result, 0);

        userDao.add(user_1);
        result = userDao.getCount();
        assertEquals(result, 1);

        userDao.add(user_2);
        result = userDao.getCount();
        assertEquals(result, 2);

        userDao.add(user_3);
        result = userDao.getCount();
        assertEquals(result, 3);
    }

    @Test
    @DisplayName("더하고 가져오고")
    void addAndGet() throws SQLException {
        assertEquals(userDao.getCount(), 0);

        userDao.add(user_1);
        userDao.add(user_2);

        int after = userDao.getCount();
        assertEquals(after, 2);

        User newUser = userDao.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());

        assertEquals(newUser.getPassword(), user_1.getPassword());
        assertNotEquals(newUser.getPassword(), user_2.getPassword());
    }

    @Test
    @DisplayName("더하고 가져오고 ( 에러 발생 )")
    void addAndGet_Exception() throws SQLException {
        assertEquals(userDao.getCount(), 0);

        userDao.add(user_1);
        userDao.add(user_2);

        int after = userDao.getCount();
        assertEquals(after, 2);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.get("unknown_id");
        });

        User newUser = userDao.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());
    }

}
