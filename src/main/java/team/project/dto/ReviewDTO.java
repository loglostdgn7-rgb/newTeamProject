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
    private byte[] imageData;
    private String base64ImageData;
    private int orderId;
    private int reviewId;        // review_Id
    private byte[] image;        // image
    private String content;      // content
    private String userId;       // userId
    private int productId;       // productId

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime at;
    private UserDTO user;
    @Setter private List<ProductDTO> elements; // 페이지네이션 대상 요소
}
