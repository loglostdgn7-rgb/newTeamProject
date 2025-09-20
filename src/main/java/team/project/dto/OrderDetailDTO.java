package team.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor //이게 있어야 jackson이 일을 한다
public class OrderDetailDTO {
    private Integer orderDetailId; //id pk
    private Integer orderId; //주문 리스트 id fk
    @JsonProperty("product_id")
    private int productId; // 개별 주문에 포함된 각 개별 상품 id fk
    private Integer quantity; //수량
    @JsonProperty("order_price")
    private Integer orderPrice;
    private String productName;
}
