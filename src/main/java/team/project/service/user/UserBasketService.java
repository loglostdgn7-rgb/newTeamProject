package team.project.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.project.dto.BasketDTO;

import java.text.NumberFormat;
import java.util.*;

@Service
public class UserBasketService {
    Logger logger = LoggerFactory.getLogger(UserBasketService.class);

    // 장바구니 담기 + 이미 담긴 상품인지 확인 및 업데이트
    public List<BasketDTO> update_basket(
            List<BasketDTO> basket,
            BasketDTO newBasket
    ) {
        logger.info("장바구니 담기/업데이트 하는 중...");
        List<BasketDTO> newBasketList = (basket == null) ? new ArrayList<>() : new ArrayList<>(basket);

        Optional<BasketDTO> existing = newBasketList.stream()
                .filter(item -> item.getProduct().getProductId() == newBasket.getProduct().getProductId())
                .findFirst();

        if (existing.isPresent()) {
            if ("add".equals(newBasket.getUpdateType())) {
                existing.get().setQuantity(existing.get().getQuantity() + newBasket.getQuantity());
            } else {
                existing.get().setQuantity(newBasket.getQuantity());
            }
        } else {
            newBasketList.add(newBasket);
        }

        logger.info("장바구니 담기/업데이트 완료!");
        return newBasketList;
    }

    //장바구니 /가격 계산
    public Map<String, String> calculate_basket_product_price(List<BasketDTO> basket) {
        logger.info("장바구니 가격 계산 중...");
        int productTotalPrice = 0;

        if (basket !=null) {
            for (BasketDTO item : basket) {
                //상품 가격 총합
                productTotalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
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

        logger.info("장바구니 가격 계산 완료!");
        return prices;
    }



}
