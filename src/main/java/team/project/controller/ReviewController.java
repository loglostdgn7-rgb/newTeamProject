package team.project.controller;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;
import team.project.dto.UserDTO;
import team.project.service.ReviewService;

import java.io.IOException;

@Slf4j
@ToString
@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 1) 페이지 열기 (review.html)
    @GetMapping("/review/view")
    public String reviewPage() {
        return "shop/product/review"; // review.html 렌더링
    }

    // 2) 리뷰 데이터 반환 (AJAX)
    @GetMapping("/review/list")
    @ResponseBody
    public PagenationDTO getReviewList(PagenationDTO pagenationDTO) {
        return reviewService.getReviews(pagenationDTO);
    }


    @PostMapping("/review/add")
    public String addReview(
            @AuthenticationPrincipal UserDTO userDTO, // 리뷰작성자
            MultipartFile imageFile, // 리뷰 작성 시 첨부한 파일
            ReviewDTO reviewDTO // 리뷰 내용
    ) {
        reviewService.addReview(reviewDTO, userDTO, imageFile);
        System.out.println(reviewDTO);

        return "redirect:/review/view";
    }

    // [추가] 포토리뷰 데이터만 반환하는 API (AJAX)
    @GetMapping("/review/list/photo")
    @ResponseBody
    public PagenationDTO getPhotoReviewList(PagenationDTO pagenationDTO) {
        return reviewService.getPhotoReviews(pagenationDTO);
    }
}
