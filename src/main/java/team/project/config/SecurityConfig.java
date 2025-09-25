package team.project.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import team.project.handler.OAuth2SuccessHandler;
import team.project.mapper.UserMapper;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private OAuth2SuccessHandler successHandler;
    @Autowired
    private UserMapper userMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(configure -> {
//            configure.disable();
//        });


        http.authorizeHttpRequests(configure -> {
            configure.requestMatchers("/", "/board/**", "/intro/**", "/shop/**",
                    "/user/duplicated","/user/login","/user/signup","/user/find-id","/user/find-psw").permitAll();
            configure.requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll();
            configure.requestMatchers("/basket").permitAll();
            configure.anyRequest().authenticated();

        });

        http.oauth2Login(configure -> {
            configure.loginPage("/user/login");
            configure.successHandler(successHandler);
        });

        http.formLogin(configure -> {
            configure.loginPage("/user/login"); // 우리가 사용하는 로그인창은 /user/login으로 GET요청을 하는 곳이다
            configure.loginProcessingUrl("/user/login"); // 로그인창에서 로그인을 시도할때는 /user/login 으로 POST 요청을 하도록 설정
            configure.usernameParameter("id"); // 로그인시 id 적는 input에 name을 id로 한다
            configure.defaultSuccessUrl("/",true); //true를 해줘야 오작동 안함
        });

        http.logout(configure -> {
            configure.logoutUrl("/user/logout"); // 로그아웃을 진행할 POST 경로
            configure.logoutSuccessUrl("/user/login"); // 로그아웃 성공 시 이동할 페이지
            configure.clearAuthentication(true);
            configure.deleteCookies("JSESSIONID");
            configure.invalidateHttpSession(true);
        });

        return http.build();
    }

    //    비밀번호 자동화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
