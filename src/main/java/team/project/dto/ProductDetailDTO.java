package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDetailDTO {
    private int detailId;
    private int productId;
    private byte[] detailImageData; // 상세 이미지
    private String productInfo;     // 제품 설명
    private String sizeInfo;        // 사이즈 정보
}