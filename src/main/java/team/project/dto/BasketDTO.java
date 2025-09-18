package team.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BasketDTO {
    private ProductDTO product;
    private int quantity;
    private String updateType;

}
