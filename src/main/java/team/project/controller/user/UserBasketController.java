package team.project.controller.user;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.project.dto.BasketDTO;
import team.project.dto.ProductDTO;
import team.project.dto.UserDTO;
import team.project.mapper.ProductMapper;
import team.project.service.user.UserBasketService;
import team.project.service.user.UserService;

import java.util.*;

@Slf4j
@RequestMapping("/user")
@Controller
public class UserBasketController {
    @Autowired
    private UserBasketService userBasketService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;


    /*****************************************************/

    //이걸 쓸거면 내껄 버려야한다. js, controller, service 잘보기
    // 장바구니 담기 + 업데이트
//    @PostMapping("/basket/update")
//    @ResponseBody
//    public ResponseEntity<String> add_to_basket(
//            @SessionAttribute(name = "basket", required = false) List<BasketDTO> basket,
//            @RequestBody BasketDTO newBasket,
//            HttpSession session
//    ) {
//        if (basket == null) basket = new ArrayList<>();
//
//        int productId = newBasket.getProduct().getId();
//        ProductDTO product = productMapper.selectProductIdDetail(productId);
//
//        if (product == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다");
//
//        BasketDTO completedBasket = new BasketDTO(product, newBasket.getQuantity(), newBasket.getUpdateType());
//
//        basket = userBasketService.update_basket(basket, completedBasket);
//
//        session.setAttribute("basket", basket);
//        return ResponseEntity.ok("상품이 장바구니에 담겼습니다.");
//    }

    @PostMapping("/basket/update")
    @ResponseBody
    public ResponseEntity<String> add_to_basket(
            @SessionAttribute(name = "basket", required = false) List<BasketDTO> basket,
            @RequestBody BasketDTO newBasket,
            HttpSession session
    ) {
        log.info("##### 1. Controller가 받은 newBasket 데이터: " + newBasket);

        if (basket == null) basket = new ArrayList<>();

        int productId = newBasket.getProduct().getId();
        ProductDTO product = productMapper.selectProductIdDetail(productId);

        if (product == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다");

        BasketDTO completedBasket = new BasketDTO(product, newBasket.getQuantity(), newBasket.getUpdateType());
        log.info("##### 2. Service로 보내기 직전 completedBasket 데이터: " + completedBasket);

        basket = userBasketService.update_basket(basket, completedBasket);

        log.info("##### 4. Service가 처리하고 반환한 최종 basket 내용: " + basket);

        session.setAttribute("basket", basket);
        return ResponseEntity.ok("상품이 장바구니에 담겼습니다.");
    }
    /*************************************************/
//    //장바구니 업데이트 
//    @ResponseBody
//    @PostMapping("/basket/update")
//    public ResponseEntity<String> get_basket_update(
//            @RequestBody BasketDTO basketUpdate,
//            HttpSession session
//    ) {
//        List<BasketDTO> basket = (List<BasketDTO>) session.getAttribute("basket");
//        if (basket != null) {
//            userBasketService.update_basket_quantity_product(
//                    basket,
//                    basketUpdate.getProduct().getId(),
//                    basketUpdate.getQuantity()
//            );
//            session.setAttribute("basket", basket);
//        }
//        return ResponseEntity.ok("basket updated");
//    }


    /*************************************************/

    //장바구니 /서머리
    @ResponseBody
    @GetMapping("/basket/summary")
    public ResponseEntity<Map<String, String>> get_basket_summary(HttpSession session) {
        List<BasketDTO> basket = (List<BasketDTO>) session.getAttribute("basket");

        Map<String, String> currencyFormattedPrices = userBasketService.calculate_basket_product_price(basket);

        if (basket == null || basket.isEmpty()) {
            Map<String, String> noPrices = new HashMap<>();
            noPrices.put("productTotalPrice", currencyFormattedPrices.get("productTotalPrice"));
            noPrices.put("shippingPrice", currencyFormattedPrices.get("shippingPrice"));
            noPrices.put("orderTotalPrice", currencyFormattedPrices.get("orderTotalPrice"));
            return ResponseEntity.ok(noPrices);
        }

        return ResponseEntity.ok(currencyFormattedPrices);
    }

    //장바구니 get
    @GetMapping("/basket")
    public String get_basket(
            @AuthenticationPrincipal UserDTO principal,
            HttpSession session,
            Model model
    ) {
        //로그인한 유저가 있으면
        if (principal != null) {
            UserDTO userForBasket = userService.get_user_by_Id(principal.getId());

            model.addAttribute("user", userForBasket);
        }

        //세션을 리스트로 가져오고
        List<BasketDTO> basket = (List<BasketDTO>) session.getAttribute("basket");
        //세션이 없으면,
        if (basket == null || basket.isEmpty()) {
            // 임시 basket 리스트 가져오기 or,
//            basket = userBasketService.create_test_list();
//            session.setAttribute("basket", basket);

            //빈 리스트를 하나 만들어준다 //위 임시 리스트를 사용할시에는 주석처리 해줘야 함
            basket = new ArrayList<>();
        }

        //서비스 가격 가져오고
        Map<String, String> currencyFormattedPrices = userBasketService.calculate_basket_product_price(basket);

        //상품 리스트 모델 걸기
        model.addAttribute("list", basket);
        //주문 가격들 모델에 걸기
        model.addAttribute("productTotalPrice", currencyFormattedPrices.get("productTotalPrice"));
        model.addAttribute("shippingPrice", currencyFormattedPrices.get("shippingPrice"));
        model.addAttribute("orderTotalPrice", currencyFormattedPrices.get("orderTotalPrice"));

        return "user/basket";
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
}
