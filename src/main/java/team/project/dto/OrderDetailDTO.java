package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class OrderDetailDTO {
    private Integer orderDetailId; //개별 주문 id
    private Integer orderId; //주문 리스트 id
    private String userId;
    private String impUid;
    private String merchantUid;
    private String payMethod;
    private String orderName;//상품리스트 이름("첫번째상품이름...")
    private String productId; // 개별 주문에 포함된 각 개별 상품 id
    private Integer quantity; //수량
    private Integer orderPrice;
    private String buyerEmail;
    private String buyerName;
    private Integer buyerTel;
    private String buyerAddr;
    private Integer buyerPostcode;
    private String buyer_request;
    private LocalDateTime createdAt;
}
