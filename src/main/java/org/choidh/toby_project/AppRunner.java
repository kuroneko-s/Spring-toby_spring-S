package org.choidh.toby_project;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDAO userDAO = context.getBean("userDAO", UserDAO.class);
        userDAO.createConnection();

        try {

        } catch (DuplicateKeyException ex) {
            SQLException sqEx = (SQLException) ex.getRootCause();
            SQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator("datasource");
            DataAccessException translate = exTranslator.translate(Thread.currentThread().getName(), null, sqEx);
        }

        CountingConnectionMaker connectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println(connectionMaker.getCount());
    }
}
