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

    public PagenationDTO getReviews(PagenationDTO reviewDTO) {

        // 전체 리뷰 수 가져오기
        int totalCount = reviewMapper.getReviewCount(reviewDTO);
        reviewDTO.setTotalElementsCount(totalCount); // 총 페이지/인덱스 계산 포함

        // 리뷰 리스트 가져오기
        List<ReviewDTO> reviews = reviewMapper.getReviewList(reviewDTO);
        reviewDTO.setReviews(reviews); // reviews 필드에 세팅


        return reviewDTO;
    }
}
