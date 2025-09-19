package team.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import team.project.dto.ProductDTO;

import java.util.List;

@Getter
@Setter
@ToString
public class PagenationDTO {
    private Integer page = 1; // 현재 페이지 번호
    private Integer size = 15; // 한 페이지에 보여줄 게시물 개수
    private Integer totalPageCount; // 전체 페이지 개수

    private Integer startPageIndex; // 현재 페이지에서 보여줄 페이지 시작 번호
    private Integer endPageIndex; // 현재 페이지에서 보여줄 페이지 끝 번호
    private Integer totalPageViewCount = 3; // 화면에 보여줄 페이지 번호 총 개수
    private Integer pageViewOffset; // 화면에 보여줄 앞 뒤 페이지 번호 개수

    private Integer totalElementsCount; // 게시물 전체 개수
    private List<ProductDTO> elements; // 페이지네이션 대상 요소 (QnABoard)

    // MyBatis에서 가져가서 사용할 수 있도록 변수 없이 getter만 생성
    public Integer getOffset(){
        return (page-1) * size;
    }






}
