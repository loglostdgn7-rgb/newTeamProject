package team.project.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(exclude = {"imageData", "base64ImageData"})
public class ProductDTO {
    private int productId;
    private String name;
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



    @Setter private List<ProductDetailDTO> elements;

}
