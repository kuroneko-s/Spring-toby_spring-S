package org.choidh.toby_project;

import org.choidh.toby_project.connection.ConnectionMaker;
import org.choidh.toby_project.connection.SimpleConnectionMaker;
import org.choidh.toby_project.domain.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDAO() {
        UserDao userDao = new UserDao(getConnectionMaker());
        return userDao;
    }

    @Bean
    public ConnectionMaker getConnectionMaker() {
        return new SimpleConnectionMaker();
    }

}
