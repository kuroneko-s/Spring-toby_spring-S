package org.choidh.toby_project.config;

import com.mysql.jdbc.Driver;
import org.choidh.toby_project.domain.dao.UserDao;
import org.choidh.toby_project.policy.DefaultUserLevelUpgradePolicy;
import org.choidh.toby_project.policy.UserLevelUpgradePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true) // AOP 설정해주는 애노테이션
@ComponentScan(basePackages = "org.choidh.toby_project")
public class AppContext {

    @Autowired
    UserDao userDao;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/toby");
        dataSource.setUsername("root");
        dataSource.setPassword("963.");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    @Bean
    public UserLevelUpgradePolicy defaultUserLevelUpgradePolicy() {
        return new DefaultUserLevelUpgradePolicy();
    }
}
