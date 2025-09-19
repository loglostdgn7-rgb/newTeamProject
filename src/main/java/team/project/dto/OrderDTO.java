package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderDTO {
    private Integer orderId;
    private String userId;
    private String impUid;
    private String merchantUid;
    private String payMethod;
    private String orderName; //상품리스트 이름("첫번째상품이름...")
    private Integer orderPrice;
    private String buyerEmail;
    private String buyerName;
    private Integer buyerTel;
    private String buyerAddr;
    private Integer buyerPostcode;
    private String buyer_request;
    private LocalDateTime createdAt;
    private List<OrderDetailDTO> productList;
}
