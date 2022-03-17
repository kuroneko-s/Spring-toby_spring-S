package org.choidh.toby_project;

import org.choidh.toby_project.domain.Level;
import org.choidh.toby_project.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = new User();
    }

    @Test
    @DisplayName("upgradeLevel() 검증")
    public void upgradeLevel() {
        final Level[] levels = Level.values();

        for (Level level: levels) {
            final Level nextLevel = level.nextLevel();
            if ( nextLevel == null ) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertEquals(user.getLevel(), level.nextLevel());
        }
    }

    @Test
    @DisplayName("upgradeLevel() Exception 검증")
    public void upgradeLevel_Exception() {
        final Level[] levels = Level.values();

        for (Level level: levels) {
            user.setLevel(level);
            if ( level.nextLevel() != null ) continue;
            assertThrows(IllegalStateException.class, () -> {
                user.setLevel(level);
                user.upgradeLevel();
            });
        }
    }

}
