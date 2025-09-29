package team.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;

import java.util.List;

@Mapper
public interface ReviewMapper {

    // 리뷰 리스트 조회
    List<ReviewDTO> getReviewList(PagenationDTO reviewDTO);

    // 리뷰 총 개수 조회
    int getReviewCount(PagenationDTO reviewDTO);


    //리뷰 된 개별 상품 아이디들. 김영수 추가 9/29
    List<Integer> selectReviewedProductIdsByOrderId(@Param("orderId") int orderId);


    /**************** 초기화 ****************/
    // 김영수 9/29
    void deleteReviewsByUserId(@Param("userId") String userId);

    List<ReviewDTO> selectDefaultReviewsByUserId(@Param("userId") String userId);

    void insertReviews(@Param("reviews") List<ReviewDTO> reviews);
}
