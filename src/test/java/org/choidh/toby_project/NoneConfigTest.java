package org.choidh.toby_project;

import org.choidh.toby_project.domain.*;
import org.choidh.toby_project.mock.MockMailSender;
import org.choidh.toby_project.mock.MockUserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.choidh.toby_project.domain.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static org.choidh.toby_project.domain.UserServiceImpl.MIN_RECOOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NoneConfigTest {

    List<User> userSample = Arrays.asList(
            new User("springex1", "CHOI1", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            new User("springex2", "CHOI2", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("springex3", "CHOI3", "1234", Level.SILVER, 60, MIN_RECOOMEND_FOR_GOLD - 1),
            new User("springex4", "CHOI4", "1234", Level.SILVER, 60, MIN_RECOOMEND_FOR_GOLD),
            new User("springex5", "CHOI5", "1234", Level.GOLD, 100, Integer.MAX_VALUE)
    );

    @Test
    @DisplayName("upgradeLevel() 고립된 테스트로 검증")
    public void upgradeLevelsWith고립() throws SQLException {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // 메일 발송의 Mock Object 생성 단계
        MockMailSender mailSender = new MockMailSender();
        userServiceImpl.setMailSender(mailSender);

        // UserDao를 Mock Object로 교체
        MockUserDao mockUserDao = new MockUserDao(this.userSample);
        userServiceImpl.setUserDao(mockUserDao);

        // 테스트용 upgrade 조건 설정
        userServiceImpl.setUpgradePolicy(new DefaultUserLevelUpgradePolicy());

        // 테스트 대상 실행
        userServiceImpl.upgradeLevels();

        final List<User> updateResult = mockUserDao.getUpdated();
        assertEquals(updateResult.size(), 2);
        checkUserAndLevel(updateResult.get(0), "springex2", Level.SILVER);
        checkUserAndLevel(updateResult.get(1), "springex4", Level.GOLD);

        // Mock Object를 이용하여 결과 확인
        List<String> requests = mailSender.getRequests();
        assertEquals(requests.size(), 2);
        assertEquals(requests.get(0), userSample.get(1).getEmail());
        assertEquals(requests.get(1), userSample.get(3).getEmail());
    }

    private void checkUserAndLevel(User user, String id, Level level) {
        assertEquals(user.getId(), id);
        assertEquals(user.getLevel(), level);
    }

    @Test
    @DisplayName("Mockito를 사용한 테스트")
    public void mockitoUpgradeLevels() {
        UserServiceImpl userService = new UserServiceImpl();

        final UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.userSample);
        userService.setUserDao(mockUserDao);

        final MailSender mockMailSender = mock(MailSender.class);
        userService.setMailSender(mockMailSender);

        final DefaultUserLevelUpgradePolicy upgradePolicy = new DefaultUserLevelUpgradePolicy();
        userService.setUpgradePolicy(upgradePolicy);

        userService.upgradeLevels();

        // 특정 값을 지정할 수 있고, any(어떤 값이든 상관없음)일때 Type만 지정해서 넣어줄 수 있나봄
        // mockUserDao가 2times 호출되어야하고, 메서드는 update, 타입은 any(어떤 값이든 상관없는데)인데 User.class타입이 들어와야함
        // times - 호출회수 검증
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));
        // userSample.get(1)가 update를 호출한적이 있는가를 검증
        verify(mockUserDao).update(userSample.get(1));
        assertEquals(userSample.get(1).getLevel(), Level.SILVER);
        verify(mockUserDao).update(userSample.get(3));
        assertEquals(userSample.get(3).getLevel(), Level.GOLD);

        // 파라미터를 정밀하게 테스트
        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        final List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues(); // capture한 값들을 가져옴
        System.out.println(mailMessages.toString());
        assertEquals(mailMessages.get(0).getTo()[0], userSample.get(1).getEmail());
        assertEquals(mailMessages.get(1).getTo()[0], userSample.get(3).getEmail());
    }


}
