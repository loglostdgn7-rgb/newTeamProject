package team.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderDTO {
    private Integer orderId; //id pk
    private String userId; //fk
    @JsonProperty("imp_uid")
    private String impUid;
    @JsonProperty("merchant_uid")
    private String merchantUid;
    @JsonProperty("order_name")
    private String orderName; //상품리스트 이름("첫번째상품이름...")
    @JsonProperty("order_price")
    private Integer orderPrice;
    @JsonProperty("buyer_name")
    private String buyerName;
    @JsonProperty("buyer_addr")
    private String buyerAddr;
    @JsonProperty("buyer_tel")
    private String buyerTel;
    @JsonProperty("buyer_request")
    private String buyerRequest;
    @JsonProperty("order_details")
    private List<OrderDetailDTO> orderDetails =  new ArrayList<>();
    private LocalDateTime orderDate;
}
