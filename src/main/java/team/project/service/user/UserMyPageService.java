package team.project.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.project.dto.BasketDTO;
import team.project.dto.ProductDTO;
import team.project.dto.SnsUserDTO;
import team.project.dto.UserDTO;
import team.project.mapper.UserMapper;

import java.util.*;

@Service
@PropertySource("file:../application.properties")
public class UserMyPageService {
    private Logger logger = LoggerFactory.getLogger(UserMyPageService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestTemplate restTemplate;
    private final Random random = new Random();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    private final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    private final String NAVER_TOKEN_URI = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_USER_INFO_URI = "https://openapi.naver.com/v1/nid/me";


    /*********************************************/
    //    프로파일 업데이트
    public void update_profile(UserDTO loginUser, UserDTO updateUser) {
        logger.info("update_user: {}", updateUser);

        //비밀번호
        if (Objects.nonNull(updateUser.getPassword()) && !updateUser.getPassword().isBlank()) {
            loginUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        //이름
        if (Objects.nonNull(updateUser.getRealName()) && !updateUser.getRealName().isBlank()) {
            loginUser.setName(updateUser.getRealName());
        }

        //우편번호
        if (Objects.nonNull(updateUser.getPostcode())) {
            loginUser.setPostcode(updateUser.getPostcode());
        }

        //도로명
        if (Objects.nonNull(updateUser.getRoadAddress()) && !updateUser.getRoadAddress().isBlank()) {
            loginUser.setRoadAddress(updateUser.getRoadAddress());
        }

        //상세수조
        if (Objects.nonNull(updateUser.getDetailAddress()) && !updateUser.getDetailAddress().isBlank()) {
            loginUser.setDetailAddress(updateUser.getDetailAddress());
        }

        //이메일
        if (Objects.nonNull(updateUser.getEmail()) && !updateUser.getEmail().isBlank()) {
            loginUser.setEmail(updateUser.getEmail());
        }

        //닉네임
        if (Objects.nonNull(updateUser.getNickname()) && !updateUser.getNickname().isEmpty()) {
            loginUser.setNickname(updateUser.getNickname());
        }

        userMapper.updateProfile(loginUser);
        logger.info("loginUser(업데이트 됨): {}", loginUser);
    }


    //프로필 로그인 연동/ sns 로그인은 핸들러로
    //oauth2 토큰
    private String request_oauth2_user_token(String clientName, String code) {
        var builder = UriComponentsBuilder.fromUriString("{uri}")
                .queryParam("client_id", "{clientName}")
                .queryParam("client_secret", "{clientSecret}")
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", code);

        var uri = switch (clientName) {
            case "kakao" -> builder.buildAndExpand(KAKAO_TOKEN_URI, KAKAO_CLIENT_ID, KAKAO_CLIENT_SECRET).toUriString();
            case "naver" -> builder.buildAndExpand(NAVER_TOKEN_URI, NAVER_CLIENT_ID, NAVER_CLIENT_SECRET).toUriString();
            default -> null;
        };
        logger.info("request uri: {}", uri);

        try {
            Map responseBody = restTemplate.getForObject(uri, Map.class);
            logger.info("responseBody: {}", responseBody);
            String accessToken = (String) responseBody.get("access_token");
            logger.info("accessToken: {}", accessToken);
            return accessToken;
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
        return null;
    }

    //access token을 통해 유저의 정보를 가져오기 => User의 SNS ID를 받아옴
    private String request_oauth2_user_info(String clientName, String token) {
        var builder = switch (clientName) {
            case "kakao" -> RequestEntity.get(KAKAO_USER_INFO_URI);
            case "naver" -> RequestEntity.get(NAVER_USER_INFO_URI);
            default -> null;
        };
        RequestEntity<Void> request = builder
                .header("Authorization", "Bearer" + " " + token)
                .build();

        //위에 request(header)를 여기 집어 넣고
        ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
        Map responseBody = response.getBody();

        return switch (clientName) {
            case "kakao" -> responseBody.get("id").toString();
            case "naver" -> ((Map) responseBody.get("response")).get("id").toString();
            default -> null;
        };
    }

    //sns 연동
    public void link_sns(UserDTO user, String clientName, String code) {
        String token = request_oauth2_user_token(clientName, code);
        if (Objects.isNull(token)) {
            return;
        }

        String snsId = request_oauth2_user_info(clientName, token);

        SnsUserDTO snsUser = SnsUserDTO.builder()
                .snsId(snsId)
                .clientName(clientName)
                .build();
        if (user.getSnsUsers() == null) {
            user.setSnsUsers(new ArrayList<>());
        }
        user.getSnsUsers().add(snsUser);

        userMapper.insertSnsUser(user.getId(), snsId, clientName);
    }

    //sns 연동 해제
    public void unlink_sns(String userId, String clientName){
        userMapper.deleteSnsUser(userId, clientName);
        logger.info("{} 사용자의 {} 연동을 해제 함", userId, clientName);
    }

    //회원 탈퇴
    public void delete_user_by_id(String userId) {
        userMapper.deleteUserById(userId);
        logger.info("{} 사용자가 탈퇴했습니다", userId);
    }

}
