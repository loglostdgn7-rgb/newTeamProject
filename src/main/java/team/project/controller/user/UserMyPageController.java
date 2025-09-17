package team.project.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team.project.dto.UserDTO;
import team.project.service.user.UserMyPageService;
import team.project.service.user.UserService;

import java.security.Principal;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Controller
public class UserMyPageController {
    @Autowired
    UserService userService;
    @Autowired
    UserMyPageService userMyPageService;

    //주문
    @GetMapping("/my-page/order")
    public void get_order(
            HttpSession session,
            Model model
    ) {
        session.getAttribute("basket");
        //여긴 상품 주문 리스트(1개가 장바구니에 담겼던 통합 주문의 리스트) 보여주기

    }

    @PostMapping("/my-page/order")
    public void post_order(
            
    ) {
        //여긴 결제 폼 내용(이름,주소등등 개인정보)받아와서 주문 "상세"에 들어가면 보이도록하고싶음
    }
    @PostMapping("/my-page/order/{orderId}")
    public void post_order_number(

    ) {
        //여긴 결제 폼 내용(이름,주소등등 개인정보)받아와서 주문 "상세"에 들어가면 보이도록하고싶음
    }


/************************************************/
    // 프로필
    @GetMapping("/my-page/profile")
    public String get_profile(
            @AuthenticationPrincipal UserDTO principal,
            Model model
    ) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        UserDTO userForView = userService.get_user_by_Id(principal.getId());
        model.addAttribute("user", userForView);

        // 전화번호 짤라서 넣기
        String userTel = userForView.getTel();
        if (userTel != null && (userTel.length() == 10 || userTel.length() == 11)) {
            String telPrev = userTel.substring(0, 3);
            String telBody = userTel.substring(3, userTel.length() - 4);
            String telTail = userTel.substring(userTel.length() - 4);

            model.addAttribute("telPrev", telPrev);
            model.addAttribute("telBody", telBody);
            model.addAttribute("telTail", telTail);
        }

        //SNS 연동 가능한 제공자 목록
        var snsUsers = userForView.getSnsUsers().stream()
                .collect(Collectors.toMap(u -> u.getClientName().toLowerCase(), u -> u));

        model.addAttribute("snsUsers", snsUsers);

        return "user/my-page/profile";
    }

    @PostMapping("/my-page/profile")
    public String post_profile(
            @AuthenticationPrincipal UserDTO loginUser,
            UserDTO updateUser,
            RedirectAttributes redirectAttributes
    ) {
        userMyPageService.update_profile(loginUser, updateUser);

        redirectAttributes.addFlashAttribute("message", "프로필 정보가 수정되었습니다");
        return "redirect:/user/my-page/profile";
    }

    // SNS 연동[로그인 한 계정을 sns 계정과 연동]
    @GetMapping("my-page/oauth2/{clientName}")
    public String get_oauth2(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable String clientName,
            @RequestParam("code") String code
    ) {
        if (user.getSnsUsers().parallelStream().noneMatch(u -> u.getClientName().equalsIgnoreCase(clientName))) {
            userMyPageService.link_sns(user, clientName, code);
        }
        return "redirect:/user/my-page/profile";
    }

    //sns 연동 해제
    @PostMapping("/my-page/unlink-sns/{clientName}")
    public String unlink_sns(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable String clientName
    ) {

        userMyPageService.unlink_sns(user.getId(), clientName);

        if (user.getSnsUsers() != null)
            user.getSnsUsers().removeIf(snsUser -> snsUser.getClientName().equalsIgnoreCase(clientName));

        return "redirect:/user/my-page/profile";
    }

    //회원 탈퇴
    @PostMapping("/delete")
    public String post_delete(
            Principal principal,
            HttpServletRequest request
    ) {
        String userId = principal.getName();
        userMyPageService.delete_user_by_id(userId);
        request.getSession().invalidate();
        return "redirect:/index";
    }

    /************************************************/

    //리뷰
    @GetMapping("/my-page/review")
    public void get_review() {

    }


}
