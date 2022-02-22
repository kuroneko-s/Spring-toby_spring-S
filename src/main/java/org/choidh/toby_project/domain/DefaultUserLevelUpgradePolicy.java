package org.choidh.toby_project.domain;

import static org.choidh.toby_project.domain.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static org.choidh.toby_project.domain.UserService.MIN_RECOOMEND_FOR_GOLD;

public class DefaultUserLevelUpgradePolicy implements UserLevelUpgradePolicy{
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
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
        user.upgradeLevel();
        this.userDao.update(user);
    }
}
