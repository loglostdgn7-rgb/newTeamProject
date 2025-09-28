package team.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;
import java.util.List;

@Mapper
public interface ReviewMapper {

    // 리뷰 리스트 조회
    List<ReviewDTO> getReviewList(PagenationDTO reviewDTO);

    // 리뷰 총 개수 조회
    int getReviewCount(PagenationDTO reviewDTO);

}
