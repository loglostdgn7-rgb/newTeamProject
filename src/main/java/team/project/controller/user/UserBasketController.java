package team.project.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
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

@RequestMapping("/user")
@Controller
public class UserBasketController {
    @Autowired
    private UserBasketService userBasketService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ObjectMapper objectMapper;

    // 장바구니 담기 + 업데이트
    @PostMapping("/basket/update")
    @ResponseBody
    public ResponseEntity<String> add_to_basket(
            @SessionAttribute(name = "basket", required = false) List<BasketDTO> basket,
            @RequestBody BasketDTO newBasket,
            HttpSession session
    ) {
        if (basket == null) basket = new ArrayList<>();

        int productId = newBasket.getProduct().getProductId();

        ProductDTO product = productMapper.selectProductIdDetail(productId);

        if (product == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다");

        BasketDTO completedBasket = new BasketDTO(product, newBasket.getQuantity(), newBasket.getUpdateType());
        basket = userBasketService.update_basket(basket, completedBasket);

        session.setAttribute("basket", basket);

        return ResponseEntity.ok("상품이 장바구니에 담겼습니다.");
    }


    //장바구니 가격 서머리
    @ResponseBody
    @GetMapping("/basket/summary")
    public ResponseEntity<String> get_basket_summary(
            @SessionAttribute(value = "basket", required = false) List<BasketDTO> basket
    ) throws JsonProcessingException {

        Map<String, String> priceMap = userBasketService.calculate_basket_product_price(basket);
        String jsonPrice = objectMapper.writeValueAsString(priceMap);

        return ResponseEntity.ok(jsonPrice);
    }

    //장바구니 get
    @GetMapping("/basket")
    public String get_basket(
            @SessionAttribute(name = "basket", required = false) List<BasketDTO> basket,
            @AuthenticationPrincipal UserDTO principal,
            Model model
    ) {
        //로그인한 유저가 있으면
        if (principal != null) {
            UserDTO user = userService.get_user_by_Id(principal.getId());

            model.addAttribute("user", user);
        }

        if (basket == null || basket.isEmpty()) {
            basket = new ArrayList<>();
        }

        //서비스 가격 가져오고
        Map<String, String> currencyFormattedPrice = userBasketService.calculate_basket_product_price(basket);

        //상품 리스트 모델 걸기
        model.addAttribute("list", basket);
        //주문 가격들 모델에 걸기
        model.addAttribute("productTotalPrice", currencyFormattedPrice.get("productTotalPrice"));
        model.addAttribute("shippingPrice", currencyFormattedPrice.get("shippingPrice"));
        model.addAttribute("orderTotalPrice", currencyFormattedPrice.get("orderTotalPrice"));

        return "user/basket";
    }


    //장바구니 / 상품 삭제
    @ResponseBody
    @DeleteMapping("/basket/delete/{productId}")
    public ResponseEntity<String> delete_basket_product(
            @SessionAttribute(name = "basket", required = false) List<BasketDTO> basket,
            @PathVariable int productId,
            HttpSession session
    ) {
        if (basket != null) {
            basket.removeIf(item -> item.getProduct().getProductId() == productId);
            session.setAttribute("basket", basket);
        }

        return ResponseEntity.ok("장바구니 상품 삭제 완료");
    }
}
