package team.project.controller.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.project.dto.BasketDTO;
import team.project.dto.BasketUpdateDTO;
import team.project.service.user.UserBasketService;
import team.project.service.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@Controller
public class UserBasketController {
    @Autowired
    private UserBasketService userBasketService;

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

        Map<String, Integer> prices = userBasketService.calculate_basket_product_price(basket);
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
//            임시 basket 리스트 가져오기 or,
            basket = userBasketService.create_test_list();
            session.setAttribute("basket", basket);

            //빈 리스트를 하나 만들어준다 //위 임시 리스트를 사용할시에는 주석처리 해줘야 함
//            basket = new ArrayList<>();
        }

        //서비스 가격 가져오고
        Map<String, Integer> prices = userBasketService.calculate_basket_product_price(basket);

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
            userBasketService.update_basket_product(
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
}
