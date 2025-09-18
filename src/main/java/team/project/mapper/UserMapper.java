package team.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.project.dto.ProductDTO;
import team.project.dto.SnsUserDTO;
import team.project.dto.UserDTO;

@Mapper
public interface UserMapper {
    //    유저 선택
    UserDTO selectUserById(String id);

    String selectUserByTel(String tel);

    UserDTO selectUserBySnsId(
            @Param("snsId") String snsId,
            @Param("clientName") String clientName
    );

    // 장바구니 / 상품 정보
    ProductDTO selectProductById(Integer productId);

    //    유저 삽입
    void insertUser(UserDTO user);

    void insertSnsUser(
            @Param("userId") String userId,
            @Param("snsId") String snsId,
            @Param("clientName") String clientName
    );

    //    유저 업데이트
    void updatePassword(
            @Param("userId") String userId,
            @Param("encodedPassword") String encodedPassword
    );

    void updateProfile(UserDTO user);

    // 유저 삭제
    void deleteUserById(String id);
    void deleteSnsUser(
            @Param("userId") String userId,
            @Param("clientName") String clientName
    );
}
