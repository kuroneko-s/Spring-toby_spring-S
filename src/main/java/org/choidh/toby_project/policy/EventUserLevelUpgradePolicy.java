package org.choidh.toby_project.policy;

import org.choidh.toby_project.domain.user.User;

import static org.choidh.toby_project.domain.user.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static org.choidh.toby_project.domain.user.UserServiceImpl.MIN_RECOOMEND_FOR_GOLD;

public class EventUserLevelUpgradePolicy implements UserLevelUpgradePolicy {
    @Override
    public boolean canUpgradeLevel(User user) {
        switch (user.getLevel()) {
            case BASIC:
                return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER - 10;
            case SILVER:
                return user.getRecommend() >= MIN_RECOOMEND_FOR_GOLD - 5;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + user.getLevel());
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
    }
}
