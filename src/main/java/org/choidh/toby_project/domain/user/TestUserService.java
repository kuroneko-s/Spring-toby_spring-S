package org.choidh.toby_project.domain.user;

import org.choidh.toby_project.exception.TestUserServiceException;

import java.util.List;

public class TestUserService extends UserServiceImpl{
    private String id = "springex4";

    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }

    @Override
    public List<User> getAll() {
        for (User user : super.getAll()) {
            super.update(user);
        }
        return null;
    }
}
