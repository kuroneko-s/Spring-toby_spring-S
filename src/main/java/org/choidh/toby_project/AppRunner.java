package org.choidh.toby_project;

import org.choidh.toby_project.connection.CountConnectionMaker;
import org.choidh.toby_project.domain.User;
import org.choidh.toby_project.domain.UserDaoJdbc;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("classpath:config/application-context.xml");
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml", "ExamplePath.class);
        User user = new User();
        user.setId("1");
        user.setName("dong");
        user.setPassword("test");

        UserDaoJdbc userDAOJdbc = new DaoFactory().userDAO();
        userDAOJdbc.deleteAll();
        userDAOJdbc.add(user);
        System.out.println(user.toString());

        User newUser = userDAOJdbc.get("1");
        System.out.println(newUser.toString());

        CountConnectionMaker connectionMaker = context.getBean("countConnectionMaker", CountConnectionMaker.class);
        System.out.println(connectionMaker.getClass().getName());

        /*
        UserDaoJdbc userDAOJdbc = context.getBean("userDAOJdbc", UserDaoJdbc.class);
        userDAOJdbc.createConnection();

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
