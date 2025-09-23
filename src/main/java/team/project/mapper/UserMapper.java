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

    List<OrderDTO> selectOrders(
            @Param("userId") String userId,
            @Param("orderId") Integer orderId
    );

    List<OrderDetailDTO> selectOrderDetailByOrderId(int orderId);

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
}
