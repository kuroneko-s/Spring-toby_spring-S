package org.choidh.toby_project;

import com.mysql.jdbc.Driver;
import org.choidh.toby_project.domain.dao.UserDao;
import org.choidh.toby_project.domain.dao.sql.EmbeddedDbSqlRegistry;
import org.choidh.toby_project.domain.dao.sql.OxmSqlService;
import org.choidh.toby_project.domain.dao.sql.SqlRegistry;
import org.choidh.toby_project.domain.dao.sql.SqlService;
import org.choidh.toby_project.domain.user.UserService;
import org.choidh.toby_project.mail.DummyMailSender;
import org.choidh.toby_project.message.MessageFactoryBean;
import org.choidh.toby_project.policy.DefaultUserLevelUpgradePolicy;
import org.choidh.toby_project.policy.UserLevelUpgradePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true) // AOP 설정해주는 애노테이션
@ComponentScan(basePackages = "org.choidh.toby_project")
public class TestApplicationConfig {

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
    public UserService testUserService() {
        UserServiceImplTest.TestUserService userService = new UserServiceImplTest.TestUserService();
        userService.setUserDao(this.userDao);
        userService.setMailSender(mailSender());
        userService.setDefaultUserLevelUpgradePolicy(defaultUserLevelUpgradePolicy());

        return userService;
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
//        sqlService.setSqlMap(new ClassPathResource("/config/sqlmap.xml"));
        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());

        return sqlRegistry;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .build();
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.choidh.toby_project.domain.dao.xjc");
        return marshaller;
    }


    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public MessageFactoryBean message() {
        return new MessageFactoryBean();
    }

    @Bean
    public UserLevelUpgradePolicy defaultUserLevelUpgradePolicy() {
        return new DefaultUserLevelUpgradePolicy();
    }
}
