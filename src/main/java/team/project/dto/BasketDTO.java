package team.project.dto;

import lombok.*;
import team.project.util.ImageUtils;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BasketDTO {
    private ProductDTO product;
    private int quantity;
    private String updateType;


    public String getBase64Image() {
        if (product != null && product.getImageData() != null) {
           return ImageUtils.imageDataUri(product.getImageData(), "image/jpeg");
        }
        return null;
    }
}
