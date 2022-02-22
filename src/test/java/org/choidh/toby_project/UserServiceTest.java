package org.choidh.toby_project;

import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.domain.*;
import org.choidh.toby_project.handler.TestUserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.choidh.toby_project.domain.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static org.choidh.toby_project.domain.UserService.MIN_RECOOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class UserServiceTest extends TestConfig{

    List<User> userSample;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    ApplicationContext context;

    @Autowired
    TestUserLevelUpgradePolicy upgradePolicy;

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
    public void upgradeLevels() {
        this.userSample.forEach(user -> this.userDao.add(user));

        this.userService.setUpgradePolicy(
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
    public void eventUserLevelUpgradePolicy() {
        this.userSample.forEach(user -> this.userDao.add(user));
        this.userService.setUpgradePolicy(
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
    @DisplayName("upgradeAllOrNothing 검증 ( transaction )")
    public void upgradeAllOrNothing() {
        this.userSample.forEach(user -> userDao.add(user));

        final User testUser = userSample.get(3);
        upgradePolicy.setId(testUser.getId());
        this.userService.setUpgradePolicy(upgradePolicy);

        try {
            userService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){ }

        checkLevel(testUser, false);
    }

}
