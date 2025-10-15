package team.project.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewDTO {
    private int reviewId;        // review_Id
    private byte[] image;        // review image
    private String base64ImageData; // Base64 Image String
    private String content;      // content
    private OrderDTO order;     // order
    private String orderId;
    private String userId;
    private UserDTO user;       // writer
    private ProductDTO product; // order product
    private ProductDetailDTO productDetail;
    private String productId; // review 테이블의 product_id

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime at;


    public void setImage(byte[] image) {
        this.image = image;
        if(image != null) {
            this.base64ImageData = "data:image/*;base64," + Base64.getEncoder().encodeToString(image);
        }
    }

    public String getBase64ImageData() {
        if (base64ImageData == null && image != null) {
            this.base64ImageData = "data:image/*;base64," + Base64.getEncoder().encodeToString(image);
        }
        return base64ImageData;
    }
}
