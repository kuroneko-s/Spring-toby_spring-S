package org.choidh.toby_project.domain;

import org.choidh.toby_project.handler.TestUserServiceException;
import org.springframework.stereotype.Component;

import static org.choidh.toby_project.domain.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static org.choidh.toby_project.domain.UserServiceImpl.MIN_RECOOMEND_FOR_GOLD;

@Component
public class TestUserLevelUpgradePolicy implements UserLevelUpgradePolicy{
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean canUpgradeLevel(User user) {
        switch (user.getLevel()) {
            case BASIC:
                return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER:
                return user.getRecommend() >= MIN_RECOOMEND_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + user.getLevel());
        }
    }

    @Override
    public void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new TestUserServiceException("Test Error");
        user.upgradeLevel();
    }

}
