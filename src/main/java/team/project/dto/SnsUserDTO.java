package team.project.dto;

import lombok.*;


import java.time.LocalDateTime;
import java.util.Map;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SnsUserDTO {
    private String snsId;
    private String clientName;
    private LocalDateTime linkedAt;
    private String accessToken;
    private Map<String, Object>attributes;
}
