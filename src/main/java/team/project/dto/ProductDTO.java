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
    private String base64ImageData; // ✨ 이 부분을 추가해줘! ✨
    //아래 추가 [김영수님](9/20)
    public void setId(Integer id) {
        this.productId = id;
    }

}
