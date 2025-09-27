package team.project.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.project.dto.*;
import team.project.mapper.UserMapper;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@PropertySource("file:../application.properties")
public class UserMyPageService {
    Logger logger = LoggerFactory.getLogger(UserMyPageService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    private final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    private final String NAVER_TOKEN_URI = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_USER_INFO_URI = "https://openapi.naver.com/v1/nid/me";


    //post 장바구니->주문 내역(orders) table에 삽입
    @Transactional //복합적인 삽입/수정에 사용
    public void save_order(OrderDTO order, UserDTO principal) {
        order.setUserId(principal.getId());

        userMapper.insertOrder(order);

        // 방금 생성된 orderId 가져오기 (xml의 useGeneratedKeys 덕분에 가능)
        int generatedOrderId = order.getOrderId();

        // 각 주문 상품(OrderDetail 테이블)에 orderId를 채워서 저장
        for (OrderDetailDTO item : order.getOrderDetails()) {
            item.setOrderId(generatedOrderId);
            userMapper.insertOrderDetail(item);
        }
        logger.info("주문 완료: {}", order.getOrderId());
    }

    //  공통 포메팅 메소드
    private void applyCommonOrderFormatting(OrderDTO order) {
        if (order == null) {
            return;
        }

        // 날짜 바꾸기
        if (Objects.nonNull(order.getOrderDate())) {
            order.setOrderDateFormatted(order.getOrderDate().format(DateTimeFormatter.ofPattern("yy.MM.dd")));
        }

        // 주문 상태 바꾸기
        if (order.getOrderStatus() != null) {
            switch (order.getOrderStatus()) {
                case "ALL" -> order.setOrderStatusFormatted("전체");
                case "PENDING" -> order.setOrderStatusFormatted("입금전");
                case "PREPARING" -> order.setOrderStatusFormatted("배송준비중");
                case "SHIPPED" -> order.setOrderStatusFormatted("배송중");
                case "DELIVERED" -> order.setOrderStatusFormatted("배송완료");
                case "CANCEL" -> order.setOrderStatusFormatted("주문취소");
                case "EXCHANGE" -> order.setOrderStatusFormatted("교환");
                case "REFUND" -> order.setOrderStatusFormatted("반품");
                default -> order.setOrderStatusFormatted(order.getOrderStatus());
            }
        } else {
            order.setOrderStatus("처리 과정에서 문제가 생겼으니 고객센터에 문의해 주세요");
        }
    }

    //주문내역 리스트 불러오기
    public PaginationDTO<OrderDTO> find_orders_by_user_id(String userId, PaginationDTO<OrderDTO> pagination) {
        // 전체 주문 개수를 조회
        int totalCount = userMapper.selectOrdersCount(userId, pagination);
        pagination.setTotalElementsCount(totalCount);

        //현재 페이지 주문 목록을 조회
        List<OrderDTO> orderList = userMapper.selectOrdersWithPagination(userId, pagination);

        logger.info("DB에서 가져온 주문 개수: {}", orderList.size());

        //merchantUid 줄이기
        for (OrderDTO order : orderList) {
            //uid 줄이기
            if (Objects.nonNull(order.getMerchantUid())) {
                String[] shorten = order.getMerchantUid().split("-");
                order.setShortMerchantUid(shorten[0] + "...");
            }

            //  공통 포메팅 메소드 적용
            applyCommonOrderFormatting(order);

            logger.info("주문내역(리스트)[ORDER_ID=[{}]] : {}", order.getOrderId(), order);
        }
        pagination.setElements(orderList);

        return pagination;
    }

    //개별(row) 주문 내역
    public OrderDTO find_order_by_id_and_user_id(
            int orderId,
            String userId
    ) {
        List<OrderDTO> orderList = userMapper.selectOrderById(userId, orderId); //유저랑 *주문번호 매칭해서 가져오기
        if (orderList == null || orderList.isEmpty()) {
            throw new IllegalArgumentException("해당 주문을 찾을 수 없습니다");
        }

        OrderDTO order = orderList.getFirst();//어차피 하나 뿐이지만 그 처음껄 가져온다. 이 안에 상품 리스트가있다.

        //  공통 포메팅 메소드 적용
        applyCommonOrderFormatting(order);

        logger.info("상세 주문 내역[ORDER_ID=[{}]] : {}", order.getOrderId(), order);

        return order;
    }

    // 주문 상태 변경
    public boolean update_order_status(int orderId, String userId, String newStatus) {
        int updatedRows = userMapper.updateOrderStatus(orderId, userId, newStatus);

        if (updatedRows == 1) {
            logger.info("주문 상태 변경 완료 orderId={} userId={} newStatus={}", orderId, userId, newStatus);
            return true;
        } else {
            logger.info("주문 상태 변경 실패 orderId={} userId={} newStatus={}", orderId, userId, newStatus);
            return false;
        }
    }


    /// ///////// 상품 상태 초기화 ////////////////////////
    @Transactional
    public int reset_test_orders_manually(String userId) {
        logger.info("수동 초기화 실행: {} 계정 주문 상태를 초기화합니다.", userId);

        return userMapper.resetOrderStatus(userId);
    }


    /*********************************************/
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


    //프로필 로그인 연동/ sns 로그인은 핸들러로
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

    //sns 연동 해제
    public void unlink_sns(String userId, String clientName) {
        userMapper.deleteSnsUser(userId, clientName);
        logger.info("{} 사용자의 {} 연동을 해제 함", userId, clientName);
    }

    //회원 탈퇴
    public void delete_user_by_id(String userId) {
        userMapper.deleteUserById(userId);
        logger.info("{} 사용자가 탈퇴했습니다", userId);
    }



}
