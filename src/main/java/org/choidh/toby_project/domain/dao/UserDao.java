package org.choidh.toby_project.domain.dao;

import org.choidh.toby_project.domain.user.User;

import java.util.List;

public interface UserDao {
    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user);
}
