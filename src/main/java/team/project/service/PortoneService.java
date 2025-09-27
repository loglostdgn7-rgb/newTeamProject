package team.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service

public class PortoneService {
    Logger logger = LoggerFactory.getLogger("PORTONE_LOGGER");

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;

    private final String ACCESS_TOKEN_URL = "https://api.iamport.kr/users/getToken";
    private final String SIMPLE_AUTH_URL = "https://api.iamport.kr/certifications/{imp_uid}";

    @Value("${portone.api.key}")
    private String API_KEY;
    @Value("${portone.api.secret}")
    private String API_SECRET;

    /// /////// 포트원 엑세스 토큰 //////////////
    public String get_access_token() {
        Map<String, String> requestBody = Map.of(
                "imp_key", API_KEY,
                "imp_secret", API_SECRET
        );
        logger.debug("requestBody : {}", requestBody);
//          토큰 요청 객체 생성
        try {
            RequestEntity<String> request = RequestEntity.post(ACCESS_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(requestBody));
            logger.debug("토큰 요청 객체 생성 완료");
//          이제 요청 하기
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            logger.debug("응답 받기 완료: status_code={} / body={}", response.getStatusCode(), response.getBody());
            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");
            String accessToken = (String) responseData.get("access_token");
            logger.debug("받은 accessToken: {}", accessToken);
            return accessToken;
//          토큰 요청 에러시
        } catch (JsonProcessingException e) {
            logger.error("requestBody 생성 에러 => " + e.getMessage());
        } catch (Exception e) {
            logger.error("그 외 에러 => " + e.getMessage());
        }
        return null;
    }

    //    포트원 간편인증 결과 조회
    private boolean get_simple_auth_result(String accessToken, String impUid, String PhoneNumber) {
        logger.debug("간편 인증 결과 조회중...");
        RequestEntity<Void> resultRequest = RequestEntity.get(SIMPLE_AUTH_URL, impUid)
                .header("Authorization", accessToken)
                .build();
        logger.debug("요청 객체 생성 완료");
        try {
            ResponseEntity<Map> response = restTemplate.exchange(resultRequest, Map.class);
            HttpStatusCode statusCode = response.getStatusCode();
            Map<String, Object> responseBody = response.getBody();
            logger.debug("응답: code={} / body={}", statusCode, responseBody);
            if (statusCode.is2xxSuccessful()) {
                Map<String, Object> data = (Map<String, Object>) responseBody.get("response");
                String phone = (String) data.get("phone");
                boolean certified = (Boolean) data.get("certified");
                logger.debug("인증시도 휴대폰: {} / 인증여부: {}", phone, certified);
                if (!phone.trim().equals(PhoneNumber.trim())) {
                    logger.debug("인증실패 - 휴대폰 번호가 다름");
                    return false;
                } else if (!certified) {
                    logger.debug("인증실패 - 인증실패한 유저");
                    return false;
                }
                logger.debug("인증성공!");
                return true;
            }
        } catch (Exception e) {
            logger.error("그 외 에러: " + e.getMessage());
        }
        return false;
    }

    //    인증 메소드
    public boolean simple_authentication(String impUid, String PhoneNumber) {
        logger.info("간편인증 진행중...");
        logger.info("1)토큰 발급중...");
        String token = get_access_token();
        if (token == null) {
            return false;
        }
        logger.info("2)본인인증 결과 조회중...");
        return get_simple_auth_result(token, impUid, PhoneNumber);
    }
}
