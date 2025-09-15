package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FindIdUserDTO {
    private String tel;
    private String authenticationKey;
}
