package team.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.project.dto.PagenationDTO;
import team.project.service.ReviewService;

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


    // 3) 리뷰 작성
//    @GetMapping("/user/my-page/order/13")
//    public String reviewAdd(PagenationDTO pagenation){
//        return
//    }
}
