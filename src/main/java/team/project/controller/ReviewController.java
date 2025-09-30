package team.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.project.dto.PagenationDTO;
import team.project.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 1) 페이지 열기 (review.html)
    @GetMapping("/view")
    public String reviewPage() {
        return "shop/product/review"; // review.html 렌더링
    }
}
