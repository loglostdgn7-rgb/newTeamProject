package team.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;
import team.project.mapper.ReviewMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    public PagenationDTO getReviews(PagenationDTO<ReviewDTO> reviewDTO) {

        // 전체 리뷰 수 가져오기
        int totalCount = reviewMapper.getReviewCount(reviewDTO);
        reviewDTO.setTotalElementsCount(totalCount); // 총 페이지/인덱스 계산 포함

        // 리뷰 리스트 가져오기
        List<ReviewDTO> reviews = reviewMapper.getReviewList(reviewDTO);
//        reviewDTO.setReviews(reviews); // reviews 필드에 세팅
        reviewDTO.setElements(reviews); // reviews 필드에 세팅


        return reviewDTO;
    }

    // 리뷰 작성
    public void addReview(ReviewDTO reviewDTO) {
        // 이미지가 없으면 null로 세팅 (텍스트 리뷰만 가능)
        if (reviewDTO.getImage() == null) {
            reviewDTO.setImage(null);
        }

        reviewMapper.insertReview(reviewDTO);
    }
}
