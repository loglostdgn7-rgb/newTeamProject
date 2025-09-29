package team.project.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewDTO {
    private int reviewId;
    private byte[] imageData;
    private String base64ImageData;
    private String content;
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime at;
    private UserDTO user;
    @Setter private List<ProductDTO> elements; // 페이지네이션 대상 요소
}
