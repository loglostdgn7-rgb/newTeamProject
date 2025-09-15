package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BasketUpdateDTO {
    private int productId;
    private int quantity;
}
