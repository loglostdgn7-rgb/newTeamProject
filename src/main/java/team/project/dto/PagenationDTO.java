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
    private  Integer pageViewOffset = 2; // 화면에 보여줄 앞 뒤 페이지 번호 개수
    /***************** 실제 Element 항목 관련 *************************/
    private Integer totalElementsCount; // 게시물 전체 개수

    @Setter
    private List<ReviewDTO> reviews; // 페이지네이션 리뷰 대상 요소

    //김영수님 수정 9/23 수정.  위에꺼에서 아래껄로 수정
    @Setter
    private List<T> elements; // 페이지네이션 대상 요소

    //---------------------------------------------------
    /***** 김영수님 9/25 수정. 변수 추가*******/
    private String startDate;
    private String endDate;
    private String status;
    /***** 김영수님 9/28 ********/
    private String searchValue;
    //---------------------------------------------------


    // MyBatis에서 가져가서 사용할 수 있도록 변수 없이 getter만 생성 (mapper xml 에서만 사용됨)
    public Integer getOffset() {
        return (page - 1) * size;
    }

    // 13개 5개 3페이지 13 / 5
    public void setTotalElementsCount(Integer totalElementsCount) {
        this.totalElementsCount = totalElementsCount;

        // 1. 전체 페이지 개수 계산
        this.totalPageCount = (int) Math.ceil((double) totalElementsCount / (double) this.size);

        // 2. 현재 페이지가 1보다 작거나 totalPageCount보다 클 경우 조정
        if (this.page < 1) {
            this.page = 1;
        } else if (this.page > this.totalPageCount && this.totalPageCount > 0) {
            this.page = this.totalPageCount;
        }

        // 3. 페이지 블록 시작 번호 계산 (totalPageViewCount=3 을 기준으로)
        // 예: 1, 4, 7... 또는 1, 6, 11... (totalPageViewCount가 5인 경우)
        int BLOCK_COUNT = 5; // 화면에 보여줄 페이지 버튼 총 개수를 5개로 가정 (pageViewOffset=2였으므로)

        // 시작 페이지 계산: ((page - 1) / BLOCK_COUNT) * BLOCK_COUNT + 1
        this.startPageIndex = ((this.page - 1) / BLOCK_COUNT) * BLOCK_COUNT + 1;

        // 4. 페이지 블록 끝 번호 계산 (전체 페이지 수를 넘지 않도록 Math.min으로 제한)
        this.endPageIndex = Math.min(this.startPageIndex + BLOCK_COUNT - 1, this.totalPageCount);

        /*
         * 원본 DTO의 복잡한 경계 조정 로직은 버그의 원인이 될 수 있어 완전히 제거했습니다.
         * 이 새로운 로직은 totalPageCount가 2일 때 endPageIndex를 2로 정확히 계산합니다.
         */
    }


}