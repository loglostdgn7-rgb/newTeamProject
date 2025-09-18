package team.project.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import team.project.dto.SnsUserDTO;
import team.project.dto.UserDTO;
import team.project.mapper.UserMapper;

import java.io.IOException;
import java.util.Objects;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);
    @Autowired
    private UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDTO principle = (UserDTO)authentication.getPrincipal();
        logger.warn("principle: " + principle.toString());
        SnsUserDTO snsUser = principle.getSnsUsers().get(0); // SNS로 로그인했으면 무조건 하나만 가진다

        // 1) 유저가 로그인한 SNS 정보를 가진 유저를 DB에서 조회해서 온다
        UserDTO user = userMapper.selectUserBySnsId(snsUser.getSnsId(), snsUser.getClientName());

        // 만약, 유저가 null이라면, DB에 회원가입 정보가 없다는 뜻이므로
        if(Objects.isNull(user)){
            authentication.setAuthenticated(false); // 로그인 여부를 해제
            request.getSession().invalidate(); // 현재 세션을 지움으로서 로그아웃 시킨다;

//            request.getSession().setAttribute("status", "NOT_LINKED");//이거 사용안함

            // 회원가입 창으로 파라미에 입력시켜서 이동시킴
            response.sendRedirect("/user/login?error=unlinked");
        } else {
            logger.info("[sns 로그인 성공] DB에서 조회된 유저 정보 {}로 principle 업데이트 합니다", user.getId());
            principle.setId(user.getId());
            principle.setName(user.getRealName());
            principle.setPostcode(user.getPostcode());
            principle.setRoadAddress(user.getRoadAddress());
            principle.setDetailAddress(user.getDetailAddress());
            principle.setTel(user.getTel());
            principle.setEmail(user.getEmail());
            principle.setNickname(user.getNickname());
            principle.setRole(user.getRole());
            principle.setSnsUsers(user.getSnsUsers());

            // 확인용 toString 을 하면 getName을 하기 때문에 실제로는 realName이 들어가서 문제가 없다. 로그때문에 헷갈린것...
//            logger.info("principle: " + principle.toString());

            response.sendRedirect("/"); // 홈페이지로 이동
        }
    }


}

