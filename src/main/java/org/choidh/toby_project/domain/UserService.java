package org.choidh.toby_project.domain;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOOMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private UserLevelUpgradePolicy upgradePolicy;
    private PlatformTransactionManager transactionManager;
    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUpgradePolicy(UserLevelUpgradePolicy upgradePolicy) {
        this.upgradePolicy = upgradePolicy;
    }

    public void upgradeLevels(){
        /*
        // 여기에 datasource를 넣음으로써 달라지네
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(this.dataSource);
        // transaction 시작 ( 내부에서 transactionManager를 초기화하는 그런 단계가 있음 )
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
         */
        TransactionStatus status =
                this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        List<User> users = this.userDao.getAll();

        try {
            users.stream()
                    .filter(user -> this.upgradePolicy.canUpgradeLevel(user))
                    .forEach(user -> {
                        this.upgradePolicy.upgradeLevel(user);
                        this.sendUpgradeEmail(user);
                    });
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }

    }

    private void sendUpgradeEmail(User user) {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost("mail.server.com"); // smtp Server Host

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("choidh.dev@gmail.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자의 등급이 " + user.getLevel().name());

        this.mailSender.send(mailMessage);
    }

    public void add(User user) {
        if ( user.getLevel() == null ) user.setLevel(Level.BASIC);
        this.userDao.add(user);
    }
}
