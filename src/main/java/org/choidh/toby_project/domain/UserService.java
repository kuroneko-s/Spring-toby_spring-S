package org.choidh.toby_project.domain;

import java.util.List;

public class UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevel() {
        List<User> users = this.userDao.getAll();

        users.forEach(user -> {
            boolean changed = false;
            switch (user.getLevel()){
                case BASIC:
                    if ( user.getLogin() >= 50 ) {
                        user.setLevel(Level.SILVER);
                        changed = true;
                    }
                    break;
                case SILVER:
                    if ( user.getRecommend() >= 30 ) {
                        user.setLevel(Level.GOLD);
                        changed = true;
                    }
                    break;
                case GOLD:
                    changed = false;
                    break;
                default:
                    throw new IllegalArgumentException("Not found Value");
            }
            if ( changed ) userDao.update(user);
        });
    }
}
