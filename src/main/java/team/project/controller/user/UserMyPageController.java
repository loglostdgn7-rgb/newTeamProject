package team.project.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team.project.dto.OrderDTO;
import team.project.dto.PagenationDTO;
import team.project.dto.ReviewDTO;
import team.project.dto.UserDTO;
import team.project.service.ReviewService;
import team.project.service.user.UserMyPageService;
import team.project.service.user.UserService;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Controller
public class UserMyPageController {
    Logger logger = LoggerFactory.getLogger(UserMyPageController.class);

    @Autowired
    UserService userService;
    @Autowired
    UserMyPageService userMyPageService;
    @Autowired
    private ReviewService reviewService;

    //장바구니 ->주문 내역 orders table에 삽입
    @ResponseBody
    @PostMapping("/payment/complete")
    public ResponseEntity<String> post_order(
            @RequestBody OrderDTO order,
            @AuthenticationPrincipal UserDTO principal,
            HttpSession session
    ) {
        userMyPageService.save_order(order, principal);

        session.removeAttribute("basket");

        return ResponseEntity.ok("주문이 성공적으로 완료 되었습니다.");
    }

    //주문 내역 리스트 보기
    @GetMapping("/my-page/order")
    public String get_order(
            @AuthenticationPrincipal UserDTO principal,
            Model model,
            PagenationDTO<OrderDTO> pagenation
    ) {
        pagenation.setSize(5); //주문내역 주문갯수
        pagenation.setPageViewOffset(1);//현재 페이지 앞뒤 번호 표시 갯수

        PagenationDTO<OrderDTO> pagenationTheOrder = userMyPageService.find_orders_by_user_id(principal.getId(), pagenation);

        model.addAttribute("pagenationTheOrder", pagenationTheOrder);

        return "user/my-page/order";
    }

    //개별 주문 내역의 상품리스트 보기
    @GetMapping("/my-page/order/{orderId}")
    public String get_order_items(
            @PathVariable("orderId") int orderId,
            @AuthenticationPrincipal UserDTO principal,
            Model model
    ) {
        OrderDTO order = userMyPageService.find_order_by_id_and_user_id(orderId, principal.getId());

        model.addAttribute("order", order);

        return "user/my-page/order-detail";
    }


    //(일괄처리) 주문상태변경
    @PostMapping("/my-page/order/{orderId}/status")
    @ResponseBody
    public ResponseEntity<String> update_order_status(
            @PathVariable int orderId,
            @RequestBody Map<String, String> statusMap,
            @AuthenticationPrincipal UserDTO principal
    ) {
        String newStatus = statusMap.get("status");

        if (newStatus == null || newStatus.isBlank()) { //널이거나 공백 포함 비었으면
            return ResponseEntity.badRequest().body("변경할 값이 없음");
        }

        boolean isSuccess = userMyPageService.update_order_status(orderId, principal.getId(), newStatus);

        if (isSuccess) {
            String message = newStatus.equals("CANCEL") ? "주문이 정상 취소 되었습니다" : "정상 반품 신청 되었습니다";
            return ResponseEntity.ok(message); //메세지를 담아서 보내기
        } else return ResponseEntity.status(403).body("주문 상태를 변경할 권한이 없거나, 주문을 찾을 수 없습니다");

    }

    //개별 상품 반품 처리
    @PostMapping("/my-page/order/detail/{orderDetailId}/refund")
    @ResponseBody
    public ResponseEntity<String> refund_single_product(
            @PathVariable int orderDetailId,
            @AuthenticationPrincipal UserDTO principal
    ) {
        boolean isSuccess = userMyPageService.refund_single_product(orderDetailId, principal.getId(), "PARTIAL_REFUND");

        if (isSuccess) return ResponseEntity.ok("해당 상품의 반품 신청이 완료되었습니다");
        else return ResponseEntity.status(403).body("반품 처리 권한이 없거나, 처리할 수 없는 상품입니다");
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

        UserDTO userForView = userService.get_user_by_Id(principal.getId()); // todo : sns로 로그인하면 이게 왜 널이지..?

        logger.info("userForView={}", userForView);

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
    public void get_review(
            @AuthenticationPrincipal UserDTO user,
            PagenationDTO<ReviewDTO> pagenation,
            Model model
    ) {
        reviewService.getUserReviews(pagenation, user);
        model.addAttribute("pagenation", pagenation);
    }


    /********************* 초기화 *************************/

    @PostMapping("/my-page/orders/reset")
    @ResponseBody
    public ResponseEntity<String> reset_orders(@AuthenticationPrincipal UserDTO principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        try {

            int updatedCount = userMyPageService.reset_test_orders_manually(principal.getId());
            return ResponseEntity.ok(updatedCount + "개 주문의 상태가 초기화되었습니다.");
        } catch (Exception e) {
            logger.error("주문 초기화 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("서버 오류로 초기화에 실패했습니다.");
        }
    }


}
