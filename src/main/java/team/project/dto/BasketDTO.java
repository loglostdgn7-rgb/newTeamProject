package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BasketDTO {
    private ProductDTO product;
    private int quantity;

    public BasketDTO(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
