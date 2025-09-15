package team.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"enabled, username, accountNonLocked, credentialsNonExpired, accountNonExpired"})
public class UserDTO implements OAuth2User, UserDetails {
    private String id;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private Integer postcode;
    @JsonIgnore
    private String roadAddress;
    @JsonIgnore
    private String detailAddress;
    @JsonIgnore
    private String tel;
    @JsonIgnore
    private String email;
    private String nickname;
    @JsonIgnore
    private String role;
    @JsonIgnore
    private ArrayList<SnsUserDTO> snsUsers; //이 유저가 연동한 SNS 유저 정보


    @JsonIgnore
    public String getRealName(){
        return this.name;
    }


    //OAuth2/////////////////////////////////////////////
    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        if (Objects.isNull(snsUsers) || snsUsers.isEmpty()) {
            return new HashMap<>();
        }
        return snsUsers.get(0).getAttributes();
    }

    @Override
    @JsonIgnore
    public String getName() {
        if (Objects.isNull(snsUsers) || snsUsers.isEmpty()) {
            return id;
        }
        return snsUsers.get(0).getClientName();
    }

    //UserDetails///////////////////////////////////////
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("READ"));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }
}

