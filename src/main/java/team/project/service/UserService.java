package team.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.project.dto.BasketDTO;
import team.project.dto.ProductDTO;
import team.project.dto.SnsUserDTO;
import team.project.dto.UserDTO;
import team.project.mapper.UserMapper;

import java.util.*;

@Service
@PropertySource("file:../application.properties")
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestTemplate restTemplate;
    private final Random random = new Random();

    @Value("spring.security.oauth2.client.registration.kakao.client-id")
    private String KAKAO_CLIENT_ID;
    @Value("spring.security.oauth2.client.registration.kakao.client-secret")
    private String KAKAO_CLIENT_SECRET;
    private final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    @Value("spring.security.oauth2.client.registration.naver.client-id")
    private String NAVER_CLIENT_ID;
    @Value("spring.security.oauth2.client.registration.naver.client-secret")
    private String NAVER_CLIENT_SECRET;
    private final String NAVER_TOKEN_URI = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_USER_INFO_URI = "https://openapi.naver.com/v1/nid/me";


    //대표 메서드
    public UserDTO get_user_by_Id(String userId) {
        return userMapper.selectUserById(userId);
    }

    //      유저 중복 확인
    //      true면 유저가 존재. 중복.
    public boolean is_user_exist(String userId) {
        logger.debug("유저 중복 조회중...");
        return Objects.nonNull(this.get_user_by_Id(userId));
    }

    /**********************************************/
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
    public ProductDTO getProductById(int productId) {
        // 실제 DB 없으니까 테스트용 더미 데이터
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
    /*********************************************/

    //회원가입
    public boolean signup(UserDTO user) {
        logger.info("회원가입 시도 중...");
        boolean duplicated = is_user_exist(user.getId());
        if (duplicated) {
            return false;
        }
        int min = 10_000_000;
        int max = 99_999_999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        user.setNickname("유저" + randomNumber);
        user.setPassword(passwordEncoder.encode(user.getPassword()));// 평문을 암호화시키고 저장
        //        DB에 insert
        userMapper.insertUser(user);
        logger.info("회원가입 완료");
        return true;
    }


    //    아이디 찾기
    public String find_user_by_tel(String tel) {
        logger.debug("전화번호로 아이디 조회 {}", tel);
        return userMapper.selectUserByTel(tel);
    }

    //    비밀번호 찾기
    public boolean check_user_by_id_and_tel(String userId, String phoneNumber) {
        try {
            logger.debug("아이디/전화번호 확인: id={}, phoneNumber={} ", userId, phoneNumber);
            UserDTO user = this.get_user_by_Id(userId);
            //        해당하는 유저 확인
            if (user != null && user.getTel().equals(phoneNumber)) {
                logger.info("아이디/번호 일치 확인");
                return true;
            }
            logger.info("아이디/번호 불일치, 해당자 없음");
            return false;
        } catch (Exception e) {
            // 예외가 발생하면 여기서 로그를 남기고 서버가 멈추는 것을 막아줍니다.
            logger.error("그 외 에러 발생: " + e.getMessage());
            return false;
        }
    }

    //     비밀번호 RESET
    public void update_password(
            String userId,
            String newPassword
    ) {
        logger.debug("비밀번호 암호화 중...");
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        logger.debug("DB 업데이트 중...");
        userMapper.updatePassword(userId, encodedNewPassword);
        logger.info("{} 사용자의 비밀번호가 변경 되었습니다", userId);
    }

    //    프로파일 업데이트
    public void update_profile(UserDTO loginUser, UserDTO updateUser) {
        logger.info("update_user: {}", updateUser);

        //비밀번호
        if (Objects.nonNull(updateUser.getPassword()) && !updateUser.getPassword().isBlank()) {
            loginUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        //이름
        if (Objects.nonNull(updateUser.getRealName()) && !updateUser.getRealName().isBlank()) {
            loginUser.setName(updateUser.getRealName());
        }

        //우편번호
        if (Objects.nonNull(updateUser.getPostcode())) {
            loginUser.setPostcode(updateUser.getPostcode());
        }

        //도로명
        if (Objects.nonNull(updateUser.getRoadAddress()) && !updateUser.getRoadAddress().isBlank()) {
            loginUser.setRoadAddress(updateUser.getRoadAddress());
        }

        //상세수조
        if (Objects.nonNull(updateUser.getDetailAddress()) && !updateUser.getDetailAddress().isBlank()) {
            loginUser.setDetailAddress(updateUser.getDetailAddress());
        }

        //이메일
        if (Objects.nonNull(updateUser.getEmail()) && !updateUser.getEmail().isBlank()) {
            loginUser.setEmail(updateUser.getEmail());
        }

        //닉네임
        if (Objects.nonNull(updateUser.getNickname()) && !updateUser.getNickname().isEmpty()) {
            loginUser.setNickname(updateUser.getNickname());
        }

        userMapper.updateProfile(loginUser);
        logger.info("loginUser(업데이트 됨): {}", loginUser);
    }


    //로그인 연동/ sns 로그인은 핸들러로
    //oauth2 토큰
    private String request_oauth2_user_token(String clientName, String code) {
        var builder = UriComponentsBuilder.fromUriString("{uri}")
                .queryParam("client_id", "{clientName}")
                .queryParam("client_secret", "{clientSecret}")
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", code);

        var uri = switch (clientName) {
            case "kakao" -> builder.buildAndExpand(KAKAO_TOKEN_URI, KAKAO_CLIENT_ID, KAKAO_CLIENT_SECRET).toUriString();
            case "naver" -> builder.buildAndExpand(NAVER_TOKEN_URI, NAVER_CLIENT_ID, NAVER_CLIENT_SECRET).toUriString();
            default -> null;
        };
        logger.info("request uri: {}", uri);

        try {
            Map responseBody = restTemplate.getForObject(uri, Map.class);
            logger.info("responseBody: {}", responseBody);
            String accessToken = (String) responseBody.get("access_token");
            logger.info("accessToken: {}", accessToken);
            return accessToken;
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
        return null;
    }

    //access token을 통해 유저의 정보를 가져오기 => User의 SNS ID를 받아옴
    private String request_oauth2_user_info(String clientName, String token) {
        var builder = switch (clientName) {
            case "kakao" -> RequestEntity.get(KAKAO_USER_INFO_URI);
            case "naver" -> RequestEntity.get(NAVER_USER_INFO_URI);
            default -> null;
        };
        RequestEntity<Void> request = builder
                .header("Authorization", "Bearer" + " " + token)
                .build();

        //위에 request(header)를 여기 집어 넣고
        ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
        Map responseBody = response.getBody();

        return switch (clientName) {
            case "kakao" -> responseBody.get("id").toString();
            case "naver" -> ((Map) responseBody.get("response")).get("id").toString();
            default -> null;
        };
    }

    //sns 연동
    public void link_sns(UserDTO user, String clientName, String code) {
        String token = request_oauth2_user_token(clientName, code);
        if (Objects.isNull(token)) {
            return;
        }

        String snsId = request_oauth2_user_info(clientName, token);

        SnsUserDTO snsUser = SnsUserDTO.builder()
                .snsId(snsId)
                .clientName(clientName)
                .build();
        if (user.getSnsUsers() == null) {
            user.setSnsUsers(new ArrayList<>());
        }
        user.getSnsUsers().add(snsUser);

        userMapper.insertSnsUser(user.getId(), snsId, clientName);
    }

    //회원 탈퇴
    public void delete_user_by_id(String userId) {
        userMapper.deleteUserById(userId);
        logger.info("{} 사용자가 탈퇴했습니다", userId);
    }

}
