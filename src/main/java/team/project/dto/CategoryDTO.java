package team.project.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {
    private int categoryId; // 카테고리 고유 번호 [ 대분류 : { 의류, 신발 } 키를 parentId에 삽입 ]
    private String name; // 카테고리 이름
    private int parentId; // 대분류 및 소분류 번호 넣어야 함
}