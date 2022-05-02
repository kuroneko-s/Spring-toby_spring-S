package org.choidh.toby_project.config;

import org.choidh.toby_project.domain.dao.sql.SqlMapConfig;
import org.choidh.toby_project.domain.user.TestUserService;
import org.choidh.toby_project.domain.user.UserService;
import org.choidh.toby_project.mail.DummyMailSender;
import org.choidh.toby_project.policy.DefaultUserLevelUpgradePolicy;
import org.choidh.toby_project.policy.UserLevelUpgradePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@EnableSqlService
@EnableTransactionManagement(proxyTargetClass = true) // AOP 설정해주는 애노테이션
@ComponentScan(basePackages = "org.choidh.toby_project")
@PropertySource("classpath:/database.properties")
public class AppContext implements SqlMapConfig {
    @Value("${db.driverClass}")
    Class<? extends Driver> driverClass;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

    //    @Bean
//    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
//        // 빈 팩토리 후처리기. 반드시 static으로 선언해야할 것
//        // 3.1~4.0 쯤에는 이 구현체를 빈으로 등록 안하면 @Value(place_holder) 값을 못읽었을 것
//        return new PropertySourcesPlaceholderConfigurer();
//    }
    private BeanDefinition definition;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(this.driverClass);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

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

    @Override
    public Resource getSqlMapResource() {
        return  new ClassPathResource("/config/sqlmap.xml");
    }

    @Profile("product")
    @Configuration
    public static class ProductAppContext{
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("test@email.com");
            return mailSender;
        }
    }

    @Profile("test")
    @Configuration
    public static class TestAppContext {
        @Bean
        public UserService testUserService() {
            return new TestUserService();
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }
}
