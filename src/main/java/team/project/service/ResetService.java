package team.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.mapper.UserMapper;

import java.util.Arrays;
import java.util.List;

@Service
public class ResetService {
    Logger logger =  LoggerFactory.getLogger(ResetService.class);

    @Autowired
    UserMapper userMapper;

    @Transactional
    public int reset_all_test_users() {
        List<String> testUserIds = Arrays.asList("user1", "user2", "user3", "user4", "user5");
        logger.info("전체 테스트 유저 초기화 시작: {}", testUserIds);

        //모든 테스트 유저의 프로필
        for (String userId : testUserIds) {
            userMapper.resetUserProfileToDefault(userId);
        }

        // 모든 테스트 유저의 주문 상태
        userMapper.resetAllTestUsersOrderStatus(testUserIds);

        logger.info("전체 테스트 유저 초기화 완료.");
        return testUserIds.size();
    }
}
