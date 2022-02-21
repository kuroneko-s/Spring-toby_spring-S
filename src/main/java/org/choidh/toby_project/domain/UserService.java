package org.choidh.toby_project.domain;

import java.util.List;

public class UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = this.userDao.getAll();

        users.forEach(user -> {
            if (canUpgradeLevel(user)) {
                this.upgradeLevel(user);
            }
        });
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        switch (user.getLevel()) {
            case BASIC:
                return user.getLogin() >= 50;
            case SILVER:
                return user.getRecommend() >= 30;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + user.getLevel());
        }
    }

    public void add(User user) {
        if ( user.getLevel() == null ) user.setLevel(Level.BASIC);
        this.userDao.add(user);
    }
}
