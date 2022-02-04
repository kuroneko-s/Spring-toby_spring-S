package org.choidh.toby_project;

import org.choidh.toby_project.connection.SimpleConnectionMaker;
import org.choidh.toby_project.domain.User;
import org.choidh.toby_project.domain.UserDao;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        User user = new User();
        user.setId("1");
        user.setName("dong");
        user.setPassword("test");

        UserDao userDAO = new UserDao(new SimpleConnectionMaker());
        userDAO.add(user);
        System.out.println(user.toString());

        User newUser = userDAO.get("1");
        System.out.println(newUser.toString());

        /*
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDAO = context.getBean("userDAO", UserDao.class);
        userDAO.createConnection();

        try {

        } catch (DuplicateKeyException ex) {
            SQLException sqEx = (SQLException) ex.getRootCause();
            SQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator("datasource");
            DataAccessException translate = exTranslator.translate(Thread.currentThread().getName(), null, sqEx);
        }

        CountingConnectionMaker connectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println(connectionMaker.getCount());
         */
    }
}