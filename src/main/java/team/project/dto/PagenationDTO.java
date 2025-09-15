package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@Getter
@Setter
@ToString
public class PagenationDTO {
    private Integer page = 1; // 현재 페이지 번호
    private Integer size = 10; // 한 페이지에 보여줄 상품 개수
    private Integer totalPageCount; // 전체 상품 개수

    private Integer startPageIndex = 1; // 현재 페이지에서 보여줄 페이지 시작 번호
    private Integer endPageIndex = 10; // 현재 페이지에서 보여줄 페이지 끝 번호
    private Integer totalPageViewCount; // 화면에 보여줄 페이지 번호 총 개수
    private Integer pageViewOffset; // 화면에 보여줄 앞 뒤 페이지 번호 개수

    private Integer totalElementsCount; // 상품 전체 갯수
    private List<ProductDTO> elements; // 페이지네이션 대상 요소 (QnABoard)

    // MyBatis에서 가져가서 사용할 수 있도록 변수 없이 getter만 생성
    public Integer getOffset(){
        return (page-1) * size;
    }
}
