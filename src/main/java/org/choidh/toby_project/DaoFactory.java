package org.choidh.toby_project;

import org.choidh.toby_project.connection.ConnectionMaker;
import org.choidh.toby_project.connection.CountConnectionMaker;
import org.choidh.toby_project.connection.SimpleConnectionMaker;
import org.choidh.toby_project.domain.JdbcContext;
import org.choidh.toby_project.domain.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDAO() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public JdbcContext jdbcContext() {
        JdbcContext jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource());
        return jdbcContext;
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:~/toby");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public ConnectionMaker countConnectionMaker() {
        CountConnectionMaker connectionMaker = new CountConnectionMaker();
        connectionMaker.setConnectionMaker(this.getConnectionMaker());
        return connectionMaker;
    }

    @Bean
    public ConnectionMaker getConnectionMaker() {
        return new SimpleConnectionMaker();
    }

}
