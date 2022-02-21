package org.choidh.toby_project;

import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.domain.Level;
import org.choidh.toby_project.domain.User;
import org.choidh.toby_project.domain.UserDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
//@DirtiesContext
class AppTests extends TestConfig{
    // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    // GenericXmlApplicationContext context = new GenericXmlApplicationContext("classpath:application-context-test.xml");
    // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context-test.xml", "ExamplePath.class);
    @Autowired private UserDao userDaoJdbc;
    @Autowired private DataSource dataSource;

    private static User user_1;
    private static User user_2;
    private static User user_3;

    @BeforeAll
    public static void beforeAll() {
        user_1 = new User("springex1", "CHOI", "1234", Level.BASIC, 1, 0);
        user_2 = new User("springex2", "DONGHYUK", "4321", Level.SILVER, 55, 10);
        user_3 = new User("springex3", "CHOIDONGHYUK", "9876", Level.GOLD, 100, 40);
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        // 아래처럼 쓰면 테스트 코드 속도가 엄청빨라짐
        // DataSource dataSource = new SingleConnectionDataSource("jdbc:h2:~/toby", "sa", "", true);
        // this.userDaoImpl.setDataSource(dataSource);

        // ~before~
        // userDaoImpl.deleteAll();

        // ~after~
        userDaoJdbc.deleteAll();
    }

    @Test
    @DisplayName("getAll() 검증_데이터가 없을때")
    public void validgetAllWithNoneData() {
        userDaoJdbc.deleteAll();
        List<User> users = userDaoJdbc.getAll();
        assertEquals(users.size(), 0);
    }

    @Test
    @DisplayName("getAll() 검증")
    public void validgetAll() {
        userDaoJdbc.add(user_1);
        List<User> user1 = userDaoJdbc.getAll();
        assertEquals(user1.size(), 1);
        checkSameUser(user1.get(0), user_1);

        userDaoJdbc.add(user_2);
        List<User> user2 = userDaoJdbc.getAll();
        assertEquals(user2.size(), 2);
        checkSameUser(user2.get(0), user_1);
        checkSameUser(user2.get(1), user_2);

        userDaoJdbc.add(user_3);
        List<User> user3 = userDaoJdbc.getAll();
        assertEquals(user3.size(), 3);
        checkSameUser(user3.get(0), user_1);
        checkSameUser(user3.get(1), user_2);
        checkSameUser(user3.get(2), user_3);
    }

    private void checkSameUser(User user, User user_1) {
        assertEquals(user.getId(), user_1.getId());
        assertEquals(user.getName(), user_1.getName());
        assertEquals(user.getPassword(), user_1.getPassword());
        assertEquals(user.getLevel(), user_1.getLevel());
        assertEquals(user.getLogin(), user_1.getLogin());
        assertEquals(user.getRecommend(), user_1.getRecommend());
    }

    @Test
    @DisplayName("deleteAllWithAnony() 검증")
    void validDeleteAllWithAnony() throws Exception {
        userDaoJdbc.deleteAll();
        assertEquals(userDaoJdbc.getCount(), 0);
        userDaoJdbc.add(user_1);
        assertEquals(userDaoJdbc.getCount(), 1);
        userDaoJdbc.deleteAll();
        assertEquals(userDaoJdbc.getCount(), 0);
    }

    @Test
    @DisplayName("getCount() 검증 - 전략 패턴 사용")
    void validGetCountWithStrategy() throws Exception{
        int result = userDaoJdbc.getCount();
        assertEquals(result, 0);

        userDaoJdbc.add(user_1);
        result = userDaoJdbc.getCount();
        assertEquals(result, 1);

        userDaoJdbc.add(user_2);
        result = userDaoJdbc.getCount();
        assertEquals(result, 2);

        userDaoJdbc.add(user_3);
        result = userDaoJdbc.getCount();
        assertEquals(result, 3);
    }

    @Test
    @DisplayName("getCount() 검증")
    void validGetCount() throws Exception {
        int result = userDaoJdbc.getCount();
        assertEquals(result, 0);

        userDaoJdbc.add(user_1);
        result = userDaoJdbc.getCount();
        assertEquals(result, 1);

        userDaoJdbc.add(user_2);
        result = userDaoJdbc.getCount();
        assertEquals(result, 2);

        userDaoJdbc.add(user_3);
        result = userDaoJdbc.getCount();
        assertEquals(result, 3);
    }

    @Test
    @DisplayName("add() 테스트 _ 전략 패턴")
    void validAddByStrategy() throws Exception {
        assertEquals(userDaoJdbc.getCount(), 0);

        userDaoJdbc.add(user_1);
        userDaoJdbc.add(user_2);

        final int after = userDaoJdbc.getCount();
        assertEquals(after, 2);

        final User newUser = userDaoJdbc.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());

        assertEquals(newUser.getPassword(), user_1.getPassword());
        assertNotEquals(newUser.getPassword(), user_2.getPassword());
    }

    @Test
    @DisplayName("add() 테스트 _ 익명클래스")
    void validAddByAnonyClass() throws Exception {
        assertEquals(userDaoJdbc.getCount(), 0);

        userDaoJdbc.add(user_1);
        userDaoJdbc.add(user_2);
        assertEquals(userDaoJdbc.getCount(), 2);

        final User newUser = userDaoJdbc.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());

        assertEquals(newUser.getPassword(), user_1.getPassword());
        assertNotEquals(newUser.getPassword(), user_2.getPassword());
    }

    @Test
    @DisplayName("더하고 가져오고")
    void addAndGet() throws SQLException {
        assertEquals(userDaoJdbc.getCount(), 0);

        userDaoJdbc.add(user_1);
        userDaoJdbc.add(user_2);

        int after = userDaoJdbc.getCount();
        assertEquals(after, 2);

        User newUser = userDaoJdbc.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());

        assertEquals(newUser.getPassword(), user_1.getPassword());
        assertNotEquals(newUser.getPassword(), user_2.getPassword());
    }

    @Test
    @DisplayName("더하고 가져오고 ( 에러 발생 )")
    void addAndGet_Exception() throws SQLException {
        assertEquals(userDaoJdbc.getCount(), 0);

        userDaoJdbc.add(user_1);
        userDaoJdbc.add(user_2);

        int after = userDaoJdbc.getCount();
        assertEquals(after, 2);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDaoJdbc.get("unknown_id");
        });

        User newUser = userDaoJdbc.get("springex1");

        assertEquals(newUser.getName(), user_1.getName());
        assertNotEquals(newUser.getName(), user_2.getName());
    }

    @Test
    @DisplayName("중복 키 테스트")
    public void duplicateKey() {
        this.userDaoJdbc.add(user_1);
        assertThrows(DuplicateKeyException.class, () -> this.userDaoJdbc.add(user_1));
    }

    @Test
    @DisplayName("SQLException을 직접 전환")
    public void translateException() {
        assertThrows(DuplicateKeyException.class, () -> {
            try {
                this.userDaoJdbc.add(user_1);
                this.userDaoJdbc.add(user_1);
            } catch (DuplicateKeyException exception) {
                SQLException sqlEx = (SQLException) exception.getRootCause();
                log.info("==================================");
                log.info(sqlEx.getClass().getName());
                // JdbcSQLIntegrityConstraintViolationException

                SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
                throw translator.translate(null, null, sqlEx);
            }
        });
    }

    @Test
    @DisplayName("update() 성공")
    public void update() throws CloneNotSupportedException {
        this.userDaoJdbc.add(user_1);
        this.userDaoJdbc.add(user_2);
        User newUser = user_1.clone();
        newUser.setName("new Test name 1");
        newUser.setPassword("new Test Password 1");
        newUser.setLevel(Level.GOLD);
        newUser.setLogin(100000);
        newUser.setRecommend(9099);

        this.userDaoJdbc.update(newUser);

        User u1 = this.userDaoJdbc.get(user_1.getId());
        User u2 = this.userDaoJdbc.get(user_2.getId());

        checkSameUser(newUser, u1);
        checkSameUser(user_2, u2);
    }
}
