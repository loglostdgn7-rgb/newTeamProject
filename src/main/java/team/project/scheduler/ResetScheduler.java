package team.project.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.project.service.ResetService;

@Component
public class ResetScheduler {
    Logger logger = LoggerFactory.getLogger(ResetScheduler.class);

    @Autowired
    private ResetService resetService;

    // cron 표현식: "초 분 시 일 월 요일"
    // 매일 0시 0분에 실행됩니다.
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleDailyReset() {
        logger.info("정기 사용자 데이터 초기화를 시작합니다 (매일 00:00 실행)");
        try {
            int updatedUserCount = resetService.reset_all_test_users();
            logger.info("정기 초기화 완료: {}명의 테스트 유저 정보가 초기화되었습니다.", updatedUserCount);
        } catch (Exception e) {
            logger.error("정기 사용자 데이터 초기화 중 오류 발생", e);
        }
    }
}

