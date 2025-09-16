package team.project.service.user;

import org.springframework.stereotype.Service;
import team.project.dto.BasketDTO;
import team.project.dto.ProductDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserBasketService {

    //임시 장바구니 상품 리스트
    //1.
//    List<Map<String, Integer>> create_test_list() {
//        List<Map<String, Integer>> basket = new ArrayList<>();
//        basket.add(Map.of("ring", 20000));
//        basket.add(Map.of("shirt", 23500));
//        basket.add(Map.of("earring", 40000));
//        basket.add(Map.of("pants", 17500));
//        basket.add(Map.of("shoes", 15000));
//        return basket;
//    }

    //2.
    public List<BasketDTO> create_test_list() {
        List<BasketDTO> list = new ArrayList<>();

        ProductDTO product1 = new ProductDTO();
        product1.setName("Half Gold / premium Earring");
        product1.setPrice(20000);
        product1.setId(1);
        list.add(new BasketDTO(product1, 1));

        ProductDTO product2 = new ProductDTO();
        product2.setName("Mc.shoes / Special.E.");
        product2.setPrice(30000);
        product2.setId(2);
        list.add(new BasketDTO(product2, 1));

        return list;
    }

    //장바구니 /가격 계산
    public Map<String, Integer> calculate_basket_product_price(List<BasketDTO> basket) {
        int productTotalPrice = 0;
        //상품 가격 총합
        for (BasketDTO item : basket) {
            productTotalPrice += item.getProduct().getPrice() * item.getQuantity();
        }

        //배송 가격
        int shippingPrice = 3000;
        if (productTotalPrice == 0) shippingPrice = 0;
        //주문 가격 총합
        int orderTotalPrice = productTotalPrice + shippingPrice;
        //가격들 담아서
        Map<String, Integer> prices = new HashMap<>();
        prices.put("productTotalPrice", productTotalPrice);
        prices.put("shippingPrice", shippingPrice);
        prices.put("orderTotalPrice", orderTotalPrice);
        //리턴하기
        return prices;
    }

    //장바구니 / 상품 업데이트
    public void update_basket_product(
            List<BasketDTO> basket,
            int productId,
            int quantity
    ) {
        if (quantity < 1) return;

        for (BasketDTO item : basket) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

}
