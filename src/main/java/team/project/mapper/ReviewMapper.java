package team.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;
import team.project.dto.UserDTO;

import java.util.List;

@Mapper
public interface ReviewMapper {

    // 유저가 작성한 리뷰 리스트 조회
    List<ReviewDTO> selectUserReviews(
            @Param("pagenation") PagenationDTO<ReviewDTO> pagenation,
            @Param("user") UserDTO user
    );

    // 유저가 작성한 리뷰 총 개수 조회
    int selectReviewCount(UserDTO user);


}
