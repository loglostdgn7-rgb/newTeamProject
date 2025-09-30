package team.project.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Base64;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"imageData", "base64ImageData"})
public class ProductDTO {
    private int productId;
    private String name;
    private int parentId;
    private int categoryId;
    private int price;
    private String productInfo;
    private String sizeInfo;
    private String material;
    private String colors;
    private String promotion;
    private byte[] imageData; 
    private String base64ImageData;
    private int salePrice;
    /***아래 추가 [김영수님](9/20)*****/
    public void setId(Integer id) {
        this.productId = id;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
        if (imageData != null && imageData.length > 0) {
            // byte[] 데이터를 Base64 문자열로 변환하여 base64ImageData 필드에 저장
            this.base64ImageData = "data:image/*;base64," + Base64.getEncoder().encodeToString(imageData);
        }
    }

    @Setter private List<ProductDetailDTO> elements;

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
        if (imageData != null) {
            this.base64ImageData = "data:image/*;base64," + Base64.getEncoder().encodeToString(imageData);
        }
    }
}
