package team.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;
import team.project.dto.UserDTO;
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
        reviewDTO.setReviews(reviews); // reviews 필드에 세팅
        reviewDTO.setElements(reviews); // reviews 필드에 세팅


        return reviewDTO;
    }

    // 리뷰 작성
    public void addReview(ReviewDTO reviewDTO, UserDTO user, MultipartFile imageFile) {
        // 작성자 설정
        reviewDTO.setUser(user);
        // 첨부파일이 있으면 설정
        if(imageFile != null) {
            try {
                reviewDTO.setImage(imageFile.getBytes());
            }catch (Exception e) {}
        }

        reviewMapper.insertReview(reviewDTO);
    }

    // [추가] 포토리뷰만 가져오는 서비스
    public PagenationDTO getPhotoReviews(PagenationDTO<ReviewDTO> reviewDTO) {
        // 포토리뷰 리스트 가져오기
        List<ReviewDTO> reviews = reviewMapper.getPhotoReviewList(reviewDTO);
        reviewDTO.setReviews(reviews); // reviews 필드에 세팅
        return reviewDTO;
    }
}
