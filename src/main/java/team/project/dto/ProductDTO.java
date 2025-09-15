package team.project.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {
    private int id;
    private String name;
    private int price;
    private String promotion;
}
