package team.project.service.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import team.project.dto.BasketDTO;
import team.project.mapper.UserMapper;

import java.text.NumberFormat;
import java.util.*;

@Service
public class UserBasketService {
    @Autowired
    UserMapper userMapper;


    //이건 장바구니 가져오기를 하면 실상 필요가 없기때문에 일단 장바구니 가져오기부터 해결하자
    //임시 장바구니 상품 리스트
//    @ModelAttribute("basketList") //여기가 아니라 파라미터에 적으셨던 것 같은데...아닌가..
//    public List<BasketDTO> create_test_list(
//            HttpSession session
//    ) {
//        List<BasketDTO> list = (List<BasketDTO>) session.getAttribute("basket"); //이게 아니었던 거 같은데..
//        if (list == null) {
//            new List<BasketDTO>;
//        }
//        return list;
//    }

    //3
    public ProductDTO getProductById(int productId) {
        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setName("테스트 상품 " + productId);
        product.setPrice(1000 * productId); // 가격 예시
        product.setPromotion("할인 없음");
        return product;
    }

    public void addBasket(List<BasketDTO> basket, int productId, int quantity) {
        // 이미 장바구니에 존재하는지 확인
        for (BasketDTO item : basket) {
            if (item.getProduct().getId() == productId) {
                // 기존 수량에 더하기
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // 새 상품이면 DB에서 Product 조회 후 추가
        ProductDTO product = getProductById(productId); // 이 메서드는 Product 조회
        if (product != null) {
            basket.add(new BasketDTO(product, quantity));
        }
    }

    //장바구니 /가격 계산
    public Map<String, String> calculate_basket_product_price(List<BasketDTO> basket) {
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
        //한국 통화로 바꾼다음
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.KOREA);

        //맵으로 담아서 보내기
        Map<String, String> prices = new HashMap<>();
        prices.put("productTotalPrice", currency.format(productTotalPrice));
        prices.put("shippingPrice", currency.format(shippingPrice));
        prices.put("orderTotalPrice", currency.format(orderTotalPrice));
        //리턴하기
        return prices;
    }

    //장바구니 / 상품 업데이트
    public void update_basket_quantity_product(
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
