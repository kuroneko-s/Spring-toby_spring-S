package org.choidh.toby_project;

import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.domain.Level;
import org.choidh.toby_project.domain.User;
import org.choidh.toby_project.domain.UserDao;
import org.choidh.toby_project.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
public class UserServiceTest extends TestConfig{

    List<User> userSample;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @BeforeEach
    public void setUp() {
        this.userSample = Arrays.asList(
                new User("springex1", "CHOI1", "1234", Level.BASIC, 49, 0),
                new User("springex2", "CHOI2", "1234", Level.BASIC, 50, 0),
                new User("springex3", "CHOI3", "1234", Level.SILVER, 60, 29),
                new User("springex4", "CHOI4", "1234", Level.SILVER, 60, 30),
                new User("springex5", "CHOI5", "1234", Level.GOLD, 100, 100)
        );
    }

    @Test
    @DisplayName("upgradeLevel() 검증")
    public void upgradeLevels() {
        this.userDao.deleteAll();

        this.userSample.forEach(user -> this.userDao.add(user));

        this.userService.upgradeLevel();

        List<User> newUsers = this.userDao.getAll();

        checkLevel(newUsers.get(0), Level.BASIC);
        checkLevel(newUsers.get(1), Level.SILVER);
        checkLevel(newUsers.get(2), Level.SILVER);
        checkLevel(newUsers.get(3), Level.GOLD);
        checkLevel(newUsers.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        assertEquals(user.getLevel(), level);
    }

}
