package team.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.project.dto.UserDTO;
import team.project.mapper.UserMapper;

import java.util.Objects;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("userName: " + username);
        UserDTO user = userMapper.selectUserById(username);
        System.out.println("조회된 유저: " + user);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User Not Found");

        }
        return user;
    }

}
