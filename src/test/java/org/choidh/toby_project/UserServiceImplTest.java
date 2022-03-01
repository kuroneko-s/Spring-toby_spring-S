package org.choidh.toby_project;

import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.domain.*;
import org.choidh.toby_project.mock.MockMailSender;
import org.choidh.toby_project.mock.MockUserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.choidh.toby_project.domain.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static org.choidh.toby_project.domain.UserServiceImpl.MIN_RECOOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class UserServiceImplTest extends TestConfig{

    List<User> userSample;

    @Autowired
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    ApplicationContext context;

    @Autowired
    TestUserLevelUpgradePolicy testUpgradePolicy;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    @BeforeEach
    public void setUp() {
        this.userDao.deleteAll();

        this.userSample = Arrays.asList(
                new User("springex1", "CHOI1", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("springex2", "CHOI2", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("springex3", "CHOI3", "1234", Level.SILVER, 60, MIN_RECOOMEND_FOR_GOLD - 1),
                new User("springex4", "CHOI4", "1234", Level.SILVER, 60, MIN_RECOOMEND_FOR_GOLD),
                new User("springex5", "CHOI5", "1234", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded)
            assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        else
            assertEquals(userUpdate.getLevel(), user.getLevel());
    }

    @Test
    @DisplayName("upgradeLevel() 검증")
    public void upgradeLevels() throws SQLException {
        this.userSample.forEach(user -> this.userDao.add(user));

        this.userServiceImpl.setUpgradePolicy(
                context.getBean("defaultUserLevelUpgradePolicy", DefaultUserLevelUpgradePolicy.class)
        );

        this.userService.upgradeLevels();

        checkLevel(userSample.get(0), false);
        checkLevel(userSample.get(1), true);
        checkLevel(userSample.get(2), false);
        checkLevel(userSample.get(3), true);
        checkLevel(userSample.get(4), false);
    }

    @Test
    @DisplayName("upgradeLevel() + Mock Object 검증")
    @DirtiesContext // context DI config에 변경이 있음을 알린다.
    public void upgradeLevelsWithMockObject() throws SQLException {
        this.userSample.forEach(user -> this.userDao.add(user));

        MockMailSender mailSender = new MockMailSender();
        this.userServiceImpl.setMailSender(mailSender);

        this.userServiceImpl.setUpgradePolicy(
                context.getBean("defaultUserLevelUpgradePolicy", DefaultUserLevelUpgradePolicy.class)
        );

        this.userService.upgradeLevels();

        checkLevel(userSample.get(0), false);
        checkLevel(userSample.get(1), true);
        checkLevel(userSample.get(2), false);
        checkLevel(userSample.get(3), true);
        checkLevel(userSample.get(4), false);

        List<String> requests = mailSender.getRequests();
        assertEquals(requests.size(), 2);
        assertEquals(requests.get(0), userSample.get(1).getEmail());
        assertEquals(requests.get(1), userSample.get(3).getEmail());
    }

    @Test
    @DisplayName("upgradeLevel() 고립된 테스트로 검증")
    public void upgradeLevelsWith고립() throws SQLException {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // 메일 발송의 Mock Object 생성 단계
        MockMailSender mailSender = new MockMailSender();
        userServiceImpl.setMailSender(mailSender);

        // UserDao를 Mock Object로 교체
        MockUserDao mockUserDao = new MockUserDao(this.userSample);
        userServiceImpl.setUserDao(mockUserDao);

        // 테스트용 upgrade 조건 설정
        userServiceImpl.setUpgradePolicy(new DefaultUserLevelUpgradePolicy());

        // 테스트 대상 실행
        userServiceImpl.upgradeLevels();

        final List<User> updateResult = mockUserDao.getUpdated();
        assertEquals(updateResult.size(), 2);
        checkUserAndLevel(updateResult.get(0), "springex2", Level.SILVER);
        checkUserAndLevel(updateResult.get(1), "springex4", Level.GOLD);

        // Mock Object를 이용하여 결과 확인
        List<String> requests = mailSender.getRequests();
        assertEquals(requests.size(), 2);
        assertEquals(requests.get(0), userSample.get(1).getEmail());
        assertEquals(requests.get(1), userSample.get(3).getEmail());
    }

    private void checkUserAndLevel(User user, String id, Level level) {
        assertEquals(user.getId(), id);
        assertEquals(user.getLevel(), level);
    }

    @Test
    @DisplayName("add() 검증")
    public void add() {
        User user1 = this.userSample.get(4);
        User user2 = this.userSample.get(0);
        user2.setLevel(null);

        this.userService.add(user1);
        this.userService.add(user2);

        final User _user1 = userDao.get(user1.getId());
        final User _user2 = userDao.get(user2.getId());

        assertEquals(user1.getLevel(), _user1.getLevel());
        assertEquals(_user2.getLevel(), Level.BASIC);
    }

    @Test
    @DisplayName("EventUserLevelUpgradePolicy 검증")
    public void eventUserLevelUpgradePolicy() throws SQLException {
        this.userSample.forEach(user -> this.userDao.add(user));
        this.userServiceImpl.setUpgradePolicy(
                context.getBean("eventUserLevelUpgradePolicy", EventUserLevelUpgradePolicy.class)
        ); // 전략 패턴
        // DI 기본으로는 Default class 주입중

        this.userService.upgradeLevels();

        checkLevel(userSample.get(0), true);
        checkLevel(userSample.get(1), true);
        checkLevel(userSample.get(2), true);
        checkLevel(userSample.get(3), true);
        checkLevel(userSample.get(4), false);
    }

    @Test
    @DisplayName("upgradeAllOrNothing 검증 With 책에 나온 내용이랑 비슷하게 ( transaction )")
    public void upgradeAllOrNothingWithBook() {
        UserServiceTx userServiceTx = new UserServiceTx();

        final User testUser = userSample.get(3);
        testUpgradePolicy.setId(testUser.getId());
//        this.userServiceImpl.setUpgradePolicy(testUpgradePolicy);
        /*
            책에서는 TestUserService라고 에러를 발생시키는 UserService를 새로 만들어서 테스트를 진행했다.
            나는 Policy를 따로 구체화해서 구현해 놓았기 때문에 굳이 클래스를 새로 만들지 않고
            테스트에 대한 정보를 새로 넣고 Impl에 그냥 Policy에 대한 구현체를 바꿔치기만 하면 성공한다.
         */
        userServiceTx.setUserService(new UserService() {
            private UserServiceImplTest _this = UserServiceImplTest.this;
            private UserDao userDao = _this.userDao;
            private UserLevelUpgradePolicy upgradePolicy= _this.testUpgradePolicy;
            private MailSender mailSender = _this.mailSender;

            public void upgradeLevels(){
                List<User> users = this.userDao.getAll();
                users.stream()
                        .filter(user -> this.upgradePolicy.canUpgradeLevel(user))
                        .forEach(user -> {
                            this.upgradePolicy.upgradeLevel(user);
                            this.sendUpgradeEmail(user);
                        });
            }

            private void sendUpgradeEmail(User user) {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setFrom("choidh.dev@gmail.com");
                mailMessage.setSubject("Upgrade 안내");
                mailMessage.setText("사용자의 등급이 " + user.getLevel().name());

                this.mailSender.send(mailMessage);
            }

            public void add(User user) {
                if ( user.getLevel() == null ) user.setLevel(Level.BASIC);
                this.userDao.add(user);
            }
        });
        userServiceTx.setTransactionManager(this.transactionManager);

        this.userSample.forEach(user -> userDao.add(user));

        try {
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (RuntimeException e){ }

        checkLevel(userSample.get(1), false);
    }

    @Test
    @DisplayName("upgradeAllOrNothing 검증 With 내가만든거 ( transaction )")
    @DirtiesContext
    public void upgradeAllOrNothing() {
        this.userSample.forEach(user -> userDao.add(user));

        final User testUser = userSample.get(3);
        testUpgradePolicy.setId(testUser.getId());
        this.userServiceImpl.setUpgradePolicy(testUpgradePolicy);
//        userService.setTransactionManager(this.transactionManager);

        try {
            userService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (RuntimeException e){ }

        checkLevel(userSample.get(1), false);
    }
}
