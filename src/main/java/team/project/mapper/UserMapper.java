package team.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.dto.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //유저 선택
    UserDTO selectUserById(String id);

    String selectUserByTel(String tel);

    UserDTO selectUserBySnsId(
            @Param("snsId") String snsId,
            @Param("clientName") String clientName
    );

    // 주문 내역
    void insertOrder(OrderDTO order);

    void insertOrderDetail(OrderDetailDTO item);

    //주문 내역<리스트> 조회
    List<OrderDTO> selectOrdersWithPagenation(
            @Param("userId") String userId,
            @Param("pagenation") PagenationDTO<OrderDTO> pagenation
    );

    //주문 내역 상품리스트
    List<OrderDTO> selectOrderById(
            @Param("userId") String userId,
            @Param("orderId") int orderId
    );

    //주문 숫자 카운트
    int selectOrdersCount(
            @Param("userId") String userId,
            @Param("pagenation") PagenationDTO<OrderDTO> pagenation
    );

    //주문 상세
    List<OrderDetailDTO> selectOrderDetailByOrderId(int orderId);

    //(일괄처리)주문 상태 변경
    int updateOrderStatus(
            @Param("orderId") int orderId,
            @Param("userId") String userId,
            @Param("newStatus") String newStatus
    );

    //개별 상품 반품
    Integer selectOrderIDByOrderDetailId(
            @Param("orderDetailId") int orderDetailId,
            @Param("userId") String userId
    );

    //refunds 테이블 삽입
    void insertRefund(int orderDetailId);


    // 유저 삽입
    void insertUser(UserDTO user);

    void insertSnsUser(
            @Param("userId") String userId,
            @Param("snsId") String snsId,
            @Param("clientName") String clientName
    );

    // 유저 비번 업데이트
    void updatePassword(
            @Param("userId") String userId,
            @Param("encodedPassword") String encodedPassword
    );


    //프로필 업데이트
    void updateProfile(UserDTO user);


    void deleteUserById(String id);

    void deleteSnsUser(
            @Param("userId") String userId,
            @Param("clientName") String clientName
    );


    /*********** 초기화 *************/
    //유저의 프로필 백업 테이블(user_defaults)의 내용으로 덮어씁
    void resetUserProfileToDefault(@Param("userId") String userId);

    // 백업 테이블(user_defaults)에서 특정 유저의 기본 프로필 정보를 조회
    UserDTO selectDefaultUserById(@Param("id") String id);

    // 특정 유저의 모든 주문(orders) 데이터 삭제
    void deleteOrdersByUserId(@Param("userId") String userId);

    // 특정 유저의 모든 주문 상세(order_detail) 데이터 삭제
    void deleteOrderDetailsByUserId(@Param("userId") String userId);

    // 특정 유저의 모든 환불(refunds) 데이터를 삭제
    void deleteRefundsByUserId(@Param("userId") String userId);

    // 백업 테이블에서 특정 유저의 기본 주문 목록을 조회
    List<OrderDTO> selectDefaultOrdersByUserId(@Param("userId") String userId);

    // 백업 테이블에서 특정 유저의 기본 주문 상세 목록을 조회
    List<OrderDetailDTO> selectDefaultOrderDetailsByUserId(@Param("userId") String userId);

    // 주문 목록을 DB에 일괄 삽입
    void insertOrders(@Param("orders") List<OrderDTO> orders);

    // 주문 상세 목록을 DB에 일괄 삽입
    void insertOrderDetails(@Param("orderDetails") List<OrderDetailDTO> orderDetails);
}
