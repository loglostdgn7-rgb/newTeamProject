package team.project.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
//---------------------------------------------------
/*****김영수님 수정. 9/23 PagenationDTO -> PagenationDTO<T>, Annotation 추가 *********/
@NoArgsConstructor
@AllArgsConstructor
public class PagenationDTO<T> {
    //----------------------------------------------------
    @Setter
    private Integer page = 1; // 현재 페이지 번호
    @Setter
    private Integer size = 16; // 한 페이지에 보여줄 게시물 개수
    @Setter
    private String sort = "wroteAt"; // 정렬 방식
    /***************** SETTER 가 없어도 되는 항목 (계산된 항목들) *********************/
    private Integer totalPageCount; // 전체 페이지 개수 (조회된 Element개수와는 상관없음)
    private Integer startPageIndex; // 현재 페이지에서 보여줄 페이지 시작 번호
    private Integer endPageIndex; // 현재 페이지에서 보여줄 페이지 끝 번호
    private final Integer totalPageViewCount = 3; // 화면에 보여줄 페이지 번호 총 개수
    //-------------------------------------------------------------------------
//    private final Integer pageViewOffset = 2; // 화면에 보여줄 앞 뒤 페이지 번호 개수
    /**** 김영수님 수정 9/24 수정. 위에꺼에서 아래껄로 변경 final -> " " ****/
    private Integer pageViewOffset = 2; // 화면에 보여줄 앞 뒤 페이지 번호 개수
    //-------------------------------------------------------------------------
    /***************** 실제 Element 항목 관련 *************************/
    private Integer totalElementsCount; // 게시물 전체 개수
    //-------------------------------------------------------------------------
    /*****김영수님 수정 9/23 수정.  위에꺼에서 아래껄로 수정*****/
    //    @Setter private List<ProductDTO> elements; // 페이지네이션 대상 요소
    @Setter
    private List<T> elements; // 페이지네이션 대상 요소

    //---------------------------------------------------
    /***** 김영수님 9/25 수정. 변수 추가*******/
    private String startDate;
    private String endDate;
    private String status;
    //---------------------------------------------------


    // MyBatis에서 가져가서 사용할 수 있도록 변수 없이 getter만 생성 (mapper xml 에서만 사용됨)
    public Integer getOffset() {
        return (page - 1) * size;
    }

    // 13개 5개 3페이지 13 / 5
    public void setTotalElementsCount(Integer totalElementsCount) {
        this.totalElementsCount = totalElementsCount;
        this.totalPageCount = (int) Math.ceil((double) totalElementsCount / (double) this.size);
        this.startPageIndex = Math.max(1, page - pageViewOffset); // 화면에 보여줄 시작 페이지 번호
        this.endPageIndex = Math.min(page + pageViewOffset, totalPageCount); // 화면에 보여줄 마지막 페이지 번호
        // 앞에 표시해야 하는 페이지 번호 개수가 부족하다면
        if (page <= pageViewOffset) {
            this.endPageIndex = Math.min(this.endPageIndex + pageViewOffset - page + 1, totalPageCount);
        }
        // 뒤에 표시해야 하는 페이지 번호 개수가 부족하다면
        if (endPageIndex - page < pageViewOffset) {
            this.startPageIndex = Math.max(this.startPageIndex - pageViewOffset + (endPageIndex - page), 1);
        }
    }


}