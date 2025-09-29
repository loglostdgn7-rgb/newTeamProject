package team.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.dto.PagenationDTO;
import team.project.dto.ProductDTO;
import team.project.dto.ReviewDTO;
import team.project.dto.UserDTO;
import team.project.mapper.ReviewMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;

    public void getUserReviews(PagenationDTO<ReviewDTO> pagenation, UserDTO user) {
        List<ReviewDTO> elements = reviewMapper.selectUserReviews(pagenation, user);
        // 화면에 표시할 요소가 있다면
        if(elements != null && !elements.isEmpty()) {
            Integer totalElementCount = reviewMapper.selectReviewCount(user);
            pagenation.setTotalElementsCount(totalElementCount);
            // 페이지네이션 객체가 실제로 화면에 표시할 데이터들을 가지게 한다
            pagenation.setElements(elements);
        }
        // 화면에 표시할 요소가 없다면
        else{
            pagenation.setTotalElementsCount(0);
            pagenation.setElements(List.of());
        }
    }
}
