package team.project.controller;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;
import team.project.service.ReviewService;

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
    public void addReview(
            ReviewDTO reviewDTO
    ) {
        reviewService.addReview(reviewDTO);
        System.out.println(reviewDTO);


//        productId - 상품Id
//        userId - 유저이름
//        orderId - 주문번호
//        image - 업로드 한 리뷰 사진
//        content - 리뷰 내용
    }

}
