package team.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team.project.dto.*;
import team.project.mapper.UserMapper;
import team.project.service.PortoneService;
import team.project.service.UserService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    PortoneService portoneService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/login")
    public String get_login(
            @AuthenticationPrincipal UserDTO user,
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {

        if (user != null) {
            return "/index";
        }

        if (error != null) {
            if ("unlinked".equals(error)) {
                model.addAttribute("errorMessage", "SNS 로그인은 로그인 후 연동이 필요합니다");
            } else {
                model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
            }
        }
        return "user/login";
    }

    //로그아웃은 세큐리티에서 해주고 있음

    //회원 가입
    @GetMapping("/signup")
    public void get_signup() {
    }

////////////// 토큰발급 테스트용 //////////////////
//    @GetMapping("/test/token")
//    @ResponseBody
//    public String testGetToken(){
//        System.out.println("포트원 토큰발급 테스트 시작");
//        String accessToken = portoneService.get_access_token();
//        System.out.println("엑세스토큰: "+accessToken);
//        if (accessToken != null) {
//            return "토큰 발급 성공! 토큰: "+accessToken;
//        }else {
//            return "토큰 발급 실패ㅠㅠㅠ";
//        }
//    }

    /// //////////////////////////////////////////


    @PostMapping("/signup")
    public String post_signup(
            UserDTO user,
            @RequestParam("authentication-key") String authenticationKey,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        logger.debug("회원가입유저 정보: {} / 인증된 imp_uid 값 {}", user, authenticationKey);
        boolean authenticated = portoneService.simple_authentication(authenticationKey, user.getTel());
        if (authenticated) {
            boolean result = userService.signup(user);//회원가입 시키고,
            if (result) {
                //성공 메세지 저장
                redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다");
            } else {
                redirectAttributes.addFlashAttribute("message", "아이디가 중복. 다시 시도해 주세요.");
                return "redirect:/user/signup?error";
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "간편인증 실패. 다시 시도해 주세요.");
            logger.error("간편인증 실패: 시도 된 번호 {}", user.getTel());
            return "redirect:/user/signup?error";
        }
        return "redirect:/user/login";
    }

    //아이디 중복체크
    @ResponseBody
    @GetMapping("/duplicated")
    public ResponseEntity<Boolean> duplicated(
            @RequestParam("id") String userId
    ) {
        boolean duplicated = userService.is_user_exist(userId);
        if (duplicated) {
            logger.info("유저 중복");
            return ResponseEntity.ok(true);
        } else {
            logger.info("유저 중복 아님");
            return ResponseEntity.ok(false);
        }
    }

    //아이디 찾기
    @GetMapping("/find-id")
    public void get_findId() {
    }

    @PostMapping("/find-id")
    @ResponseBody
    public ResponseEntity<Map<String, String>> post_findId(
            @RequestBody FindIdUserDTO findIdUser
    ) {
        logger.debug("아이디 찾기 요청 받음: tel={}, key={}", findIdUser.getTel(), findIdUser.getAuthenticationKey());
        boolean isAuthenticated = portoneService.simple_authentication(findIdUser.getAuthenticationKey(), findIdUser.getTel());
        if (isAuthenticated) {
            String foundId = userService.find_user_by_tel(findIdUser.getTel());
            if (foundId != null) {
                return ResponseEntity.ok(Map.of("userId", foundId));
            }
        }
        logger.warn("포트원 본인인증 실패");
        return ResponseEntity.ok(Collections.emptyMap());

    }


    //비밀번호 찾기
    @GetMapping("/find-psw")
    public void get_findPsw() {
    }

    @PostMapping("/find-psw")
    @ResponseBody
    public ResponseEntity<Boolean> post_findPsw(
            @RequestBody FindPswUserDTO findPswUser,
            HttpSession session
    ) {
        logger.debug("비밀번호 찾기 요청 받음: id={}, tel={}, key={}",
                findPswUser.getId(), findPswUser.getTel(), findPswUser.getAuthenticationKey());
//        포트원 본인인증
        boolean isAuthenticated = portoneService.simple_authentication(
                findPswUser.getAuthenticationKey(), findPswUser.getTel()
        );
        if (!isAuthenticated) {
            logger.warn("포트원 본인인증 실패");
            return ResponseEntity.ok(false);
        }
//        본인 인증 됐다면 진행
        boolean isUserExist = userService.check_user_by_id_and_tel(findPswUser.getId(), findPswUser.getTel());
        if (isUserExist) {
            session.setAttribute("pswResetUser", findPswUser.getId());
        }
        return ResponseEntity.ok(isUserExist);
    }


    //비밀번호 RESET
    @GetMapping("/reset-psw")
    public String get_resetPsw(
            HttpSession session
    ) {
        if (session.getAttribute("pswResetUser") == null) {
            return "redirect:/user/login";
        }
        return "user/reset-psw";

    }

    @PostMapping("/reset-psw")
    public String post_resetPsw(
            @RequestParam("password") String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        logger.debug("비밀번호 RESET 세션 확인중...");
        String userId = (String) session.getAttribute("pswResetUser");
        if (userId == null) {
            return "redirect:/user/login";
        }
        logger.debug("세션 확인됨. 비밀번호 업데이트 중...");
        userService.update_password(userId, newPassword);
        session.removeAttribute("pswResetUser");
        redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경 되었습니다");
        return "redirect:/user/login";
    }

    /*****************************************************/
    //장바구니 /서머리
    @ResponseBody
    @GetMapping("/basket/summary")
    public ResponseEntity<Map<String, Integer>> get_basket_summary(HttpSession session) {
        List<BasketDTO> basket = (List<BasketDTO>) session.getAttribute("basket");

        if (basket == null || basket.isEmpty()) {
            Map<String, Integer> noPrices = new HashMap<>();
            noPrices.put("productTotalPrice", 0);
            noPrices.put("shippingPrice", 0);
            noPrices.put("orderTotalPrice", 0);
            return ResponseEntity.ok(noPrices);
        }

        Map<String, Integer> prices = userService.calculate_basket_product_price(basket);
        return ResponseEntity.ok(prices);
    }

    //장바구니 get
    @GetMapping("/basket")
    public String get_basket(
            HttpSession session,
            Model model
    ) {
        //세션을 리스트로 가져오고
        List<BasketDTO> basket = (List<BasketDTO>) session.getAttribute("basket");
        //세션이 없으면,
        if (basket == null || basket.isEmpty()) {
            //임시 basket 리스트 가져오기 or,
//            basket = userService.create_test_list();
//            session.setAttribute("basket", basket);

            //빈 리스트를 하나 만들어준다
            basket = new ArrayList<>();
        }

        //서비스 가격 가져오고
        Map<String, Integer> prices = userService.calculate_basket_product_price(basket);

        //상품 리스트 모델 걸기
        model.addAttribute("list", basket);
        //주문 가격들 모델에 걸기
        model.addAttribute("productTotalPrice", prices.get("productTotalPrice"));
        model.addAttribute("shippingPrice", prices.get("shippingPrice"));
        model.addAttribute("orderTotalPrice", prices.get("orderTotalPrice"));

        return "user/basket";
    }

    //장바구니 / 상품 수량 업데이트
    @ResponseBody
    @PostMapping("/basket/update")
    public ResponseEntity<String> get_basket_update(
            @RequestBody BasketUpdateDTO basketUpdate,
            HttpSession session
    ) {
        List<BasketDTO> basket = (List<BasketDTO>) session.getAttribute("basket");
        if (basket != null) {
            userService.update_basket_product(
                    basket,
                    basketUpdate.getProductId(),
                    basketUpdate.getQuantity()
            );
            session.setAttribute("basket", basket);
        }
        return ResponseEntity.ok("basket updated");
    }

    //장바구니 / 상품 삭제
    @ResponseBody
    @DeleteMapping("/basket/delete/{productId}")
    public ResponseEntity<String> delete_basket_product(
            @PathVariable int productId,
            HttpSession session
    ) {


        List<BasketDTO> basket = (List<BasketDTO>) session.getAttribute("basket");
        if (basket != null) {
            basket.removeIf(item -> item.getProduct().getId() == productId);
            session.setAttribute("basket", basket);
        }

        return ResponseEntity.ok("basket product deleted");
    }

    /*************************************************/
    @GetMapping("/my-page/order")
    public void get_order() {

    }

    //    Profile
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

    /****************************************/
    @PostMapping("/my-page/profile")
    public String post_profile(
            @AuthenticationPrincipal UserDTO loginUser,
            UserDTO updateUser,
            RedirectAttributes redirectAttributes
    ) {
        userService.update_profile(loginUser, updateUser);

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
            userService.link_sns(user, clientName, code);
        }
        return "redirect:/user/my-page/profile";
    }

    //sns 연동 해제
    @PostMapping("/my-page/unlink-sns/{clientName}")
    public String unlink_sns(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable String clientName
    ) {

        userService.unlink_sns(user.getId(), clientName);

        if (user.getSnsUsers() != null)
            user.getSnsUsers().removeIf(snsUser -> snsUser.getClientName().equalsIgnoreCase(clientName));

        return "redirect:/user/my-page/profile";
    }

    /********************************************/

    @GetMapping("/my-page/review")
    public void get_review() {

    }

    //회원 탈퇴
    @PostMapping("/delete")
    public String post_delete(
            Principal principal,
            HttpServletRequest request
    ) {
        String userId = principal.getName();
        userService.delete_user_by_id(userId);
        request.getSession().invalidate();
        return "redirect:/index";
    }

}
