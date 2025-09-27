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
    List<OrderDTO> selectOrdersWithPagination(
            @Param("userId") String userId,
            @Param("pagination") PaginationDTO<OrderDTO> pagination
    );
    //주문 내역 상품리스트
    List<OrderDTO> selectOrderById(
            @Param("userId") String userId,
            @Param("orderId") int orderId
    );

    //주문 숫자 카운트
    int selectOrdersCount(
            @Param("userId")  String userId,
            @Param("pagination") PaginationDTO<OrderDTO> pagination
    );

    //주문 상세
    List<OrderDetailDTO> selectOrderDetailByOrderId(int orderId);

    //주문 상태 변경
    int updateOrderStatus(
            @Param("orderId") int orderId,
            @Param("userId") String userId,
            @Param("newStatus") String newStatus
    );


    // 장바구니, 상품 정보
//    ProductDTO selectProductById(Integer productId);

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

/**************************/
    //유저 초기화
    UserDTO selectDefaultUserById(String id);

    //주문상태초기화
    int resetOrderStatus(@Param("userId") String userId);

    //관리자 모두 초기화
    void resetUserProfileToDefault(@Param("userId") String userId);

    int resetAllTestUsersOrderStatus(@Param("testUserIds") List<String> testUserIds);

}
