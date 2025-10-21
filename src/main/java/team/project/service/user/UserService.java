package team.project.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.project.dto.UserDTO;
import team.project.mapper.UserMapper;

import java.util.*;

@Service

public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired

    private RestTemplate restTemplate;
    private final Random random = new Random();

    //유저 가져오기
    public UserDTO get_user_by_Id(String userId) {
        return userMapper.selectUserById(userId);
    }

    // 유저 중복 확인
    // true면 유저가 존재. 중복.
    public boolean is_user_exist(String userId) {
        logger.debug("유저 중복 조회중...");
        return Objects.nonNull(this.get_user_by_Id(userId));
    }

    //회원가입
    public boolean signup(UserDTO user) {
        logger.info("회원가입 시도 중...");
        boolean duplicated = is_user_exist(user.getId());
        if (duplicated) {
            return false;
        }
        int min = 10_000_000;
        int max = 99_999_999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        user.setNickname("유저" + randomNumber);
        user.setPassword(passwordEncoder.encode(user.getPassword()));// 평문을 암호화시키고 저장
        //        DB에 insert
        userMapper.insertUser(user);
        logger.info("회원가입 완료");
        return true;
    }


    //    아이디 찾기
    public String find_user_by_tel(String tel) {
        logger.debug("전화번호로 아이디 조회 {}", tel);
        return userMapper.selectUserByTel(tel);
    }

    //    비밀번호 찾기
    public boolean check_user_by_id_and_tel(String userId, String phoneNumber) {
        try {
            logger.debug("아이디/전화번호 확인: id={}, phoneNumber={} ", userId, phoneNumber);
            UserDTO user = this.get_user_by_Id(userId);
            //        해당하는 유저 확인
            if (user != null && user.getTel().equals(phoneNumber)) {
                logger.info("아이디/번호 일치 확인");
                return true;
            }
            logger.info("아이디/번호 불일치, 해당자 없음");
            return false;
        } catch (Exception e) {
            // 예외가 발생하면 여기서 로그를 남기고 서버가 멈추는 것을 막아줍니다.
            logger.error("그 외 에러 발생: " + e.getMessage());
            return false;
        }
    }

    //     비밀번호 RESET
    public void update_password(
            String userId,
            String newPassword
    ) {
        logger.debug("비밀번호 암호화 중...");
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        logger.debug("DB 업데이트 중...");
        userMapper.updatePassword(userId, encodedNewPassword);
        logger.info("{} 사용자의 비밀번호가 변경 되었습니다", userId);
    }





    /********* 유저 초기화 **************/
    public void reset_profile(String id) {
        UserDTO defaultUser = userMapper.selectDefaultUserById(id);

        if (defaultUser == null) {
            throw new IllegalArgumentException("초기화할 수 없는 사용자입니다.");
        }

        // 2. 현재 사용자의 정보를 원본 데이터로 덮어쓰기
        UserDTO userToReset = userMapper.selectUserById(id);
        userToReset.setId(defaultUser.getId());
        userToReset.setName(defaultUser.getRealName());
        userToReset.setPassword(passwordEncoder.encode(defaultUser.getPassword()));
        userToReset.setPostcode(defaultUser.getPostcode());
        userToReset.setRoadAddress(defaultUser.getRoadAddress());
        userToReset.setDetailAddress(defaultUser.getDetailAddress());
        userToReset.setTel(defaultUser.getTel());
        userToReset.setEmail(defaultUser.getEmail());
        userToReset.setNickname(defaultUser.getNickname());

        userMapper.updateProfile(userToReset);
    }



}
