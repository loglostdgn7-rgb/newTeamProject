package team.project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigApiController {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @GetMapping("/api/config")
    public Map<String, String> get_config() {
        Map<String, String> map = new HashMap<>();
        map.put("KAKAO_CLIENT_ID", KAKAO_CLIENT_ID);
        map.put("NAVER_CLIENT_ID", NAVER_CLIENT_ID);
        return map;
    }


}
