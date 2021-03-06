package org.choidh.toby_project.domain.user;

import org.choidh.toby_project.domain.Level;
import org.choidh.toby_project.domain.dao.UserDao;
import org.choidh.toby_project.policy.UserLevelUpgradePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "userService")
public class UserServiceImpl implements UserService{
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOOMEND_FOR_GOLD = 30;

    @Autowired
    protected UserDao userDao;

    @Autowired
    private UserLevelUpgradePolicy defaultUserLevelUpgradePolicy;

    @Autowired
    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDefaultUserLevelUpgradePolicy(UserLevelUpgradePolicy defaultUserLevelUpgradePolicy) {
        this.defaultUserLevelUpgradePolicy = defaultUserLevelUpgradePolicy;
    }

    public void upgradeLevels(){
        List<User> users = this.userDao.getAll();
        users.stream()
                .filter(user -> this.defaultUserLevelUpgradePolicy.canUpgradeLevel(user))
                .forEach(user -> {
//                    this.upgradePolicy.upgradeLevel(user);
                    this.upgradeLevel(user);
                    this.userDao.update(user);
                    this.sendUpgradeEmail(user);
                });
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
    }

    private void sendUpgradeEmail(User user) {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost("mail.server.com"); // smtp Server Host

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("choidh.dev@gmail.com");
        mailMessage.setSubject("Upgrade ??????");
        mailMessage.setText("???????????? ????????? " + user.getLevel().name());

        this.mailSender.send(mailMessage);
    }

    public void add(User user) {
        if ( user.getLevel() == null ) user.setLevel(Level.BASIC);
        this.userDao.add(user);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }
}
