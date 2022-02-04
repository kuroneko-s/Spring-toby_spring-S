package org.choidh.toby_project.domain;

import lombok.Setter;

@Setter
public class User {
    String id;
    String name;
    String password;

    public String getId() {
        return id == null ? "" : id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}