package org.choidh.toby_project.config;

import org.choidh.toby_project.UserServiceImplTest;
import org.choidh.toby_project.domain.user.UserService;
import org.choidh.toby_project.mail.DummyMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Configuration
public class TestAppContext {
    @Bean
    public UserService testUserService() {
        return new UserServiceImplTest.TestUserService();
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }
}
