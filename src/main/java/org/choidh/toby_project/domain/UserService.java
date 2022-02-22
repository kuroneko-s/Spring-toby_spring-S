package org.choidh.toby_project.domain;

import java.util.List;

public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOOMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private UserLevelUpgradePolicy upgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUpgradePolicy(UserLevelUpgradePolicy upgradePolicy) {
        this.upgradePolicy = upgradePolicy;
    }

    public void upgradeLevels() {
        List<User> users = this.userDao.getAll();

        users.forEach(user -> {
            if (this.upgradePolicy.canUpgradeLevel(user)) {
                this.upgradePolicy.upgradeLevel(user);
            }
        });
    }

    public void add(User user) {
        if ( user.getLevel() == null ) user.setLevel(Level.BASIC);
        this.userDao.add(user);
    }
}
