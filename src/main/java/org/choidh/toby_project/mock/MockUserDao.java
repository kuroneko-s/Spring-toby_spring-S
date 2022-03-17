package org.choidh.toby_project.mock;

import org.choidh.toby_project.domain.user.User;
import org.choidh.toby_project.domain.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {
    private List<User> users; // upgrade by request
    private List<User> updated = new ArrayList<>(); // upgraded users

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    public List<User> getUpdated() {
        return this.updated;
    }
    @Override
    public void update(User user) {
        updated.add(user);
    }

    @Override
    public List<User> getAll() {
        return this.users;
    }

    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }



    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }


}
