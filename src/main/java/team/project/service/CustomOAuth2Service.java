package team.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import team.project.dto.SnsUserDTO;
import team.project.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    Logger logger = LoggerFactory.getLogger(CustomOAuth2Service.class);


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        logger.info("oAuth2User: " + oAuth2User);
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String accessTokenValue = accessToken.getTokenValue();
        //어떤 사이트로 로그인 시도했느냐 확인
        String clientName = userRequest.getClientRegistration().getClientName().toLowerCase();
        String snsId = switch (clientName) {
            case "kakao" -> oAuth2User.getAttribute("id").toString();
            case "naver" -> {
                Map<String, String> response = (Map)oAuth2User.getAttribute("response");
                yield response.get("id");
            }
            default -> null;
        };
        if (snsId == null) {
            throw new OAuth2AuthenticationException("snsId is null. clientName: " + clientName);
        }
        //sns 정보를 가진 유저를 생성하고
        SnsUserDTO snsUser= SnsUserDTO.builder()
                .snsId(snsId)
                .clientName(clientName)
                .accessToken(accessTokenValue)
                .attributes(oAuth2User.getAttributes())
                .build();
        logger.info("snsUser: " + snsUser);
        //우리 어플리케이션에서 사용하는 유저를 생성하고 sns 정보 넣은다음
        //일단 무조건 로그인 시킨다
        return UserDTO.builder()
                .id("Anonymous")
                .snsUsers(new ArrayList<>(List.of(snsUser)))
                .build();
    }
}
