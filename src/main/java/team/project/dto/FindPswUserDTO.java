package team.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FindPswUserDTO {
    private String id;
    private String tel;
    private String authenticationKey;

}
