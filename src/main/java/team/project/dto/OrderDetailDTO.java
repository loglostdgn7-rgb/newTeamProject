package team.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import team.project.util.ImageUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"productImage", "base64Image"})
@NoArgsConstructor //이게 있어야 jackson이 일을 한다
public class OrderDetailDTO {
    private Integer orderDetailId; //id pk
    private Integer orderId; //주문 리스트 id fk
    private Integer quantity; //수량
    private String productName;
    private byte[] productImage; //이미지
    @JsonProperty("product_id")
    private int productId; // 개별 주문에 포함된 각 개별 상품 id fk
    @JsonProperty("product_price")
    private Integer productPrice;

    private String base64Image;

    public String getBase64Image() {
        if (base64Image != null) {
            return base64Image;
        }
        //아니면,
        if (this.productImage != null) {
            this.base64Image = ImageUtils.imageDataUri(this.getProductImage(), "image/jpeg");
            return this.base64Image;
        }
        //그래도 아니면,
        return null;
    }
}
