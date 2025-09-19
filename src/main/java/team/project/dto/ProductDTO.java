package team.project.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {
    private int productId;
    private String name;
    private int categoryId;
    private int price;
    private String promotion;
    private byte[] imageData; // 이미지 데이터를 byte 배열로 저장
}
