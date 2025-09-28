package team.project.mapper;

import org.apache.ibatis.annotations.Param;
import team.project.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {

    // ----------------------------
    // 제품(Product) 조회 / 상세 / 등록
    // ----------------------------

    // 특정 제품 정보 조회 (DTO 조건 기반)
    List<ProductDTO> get_product_info(ProductDTO productDTO);

    // 페이지네이션 적용한 제품 리스트 조회
    List<ProductDTO> selectProducts(PagenationDTO pagenation);

    // 특정 카테고리(parentId) 제품 리스트 조회 (페이징 포함)
    List<ProductDTO> selectProductsCategory(@Param("parentId") int parentId, PagenationDTO pagenation);

    // 단일 제품의 상세 정보 조회
    List<ProductDetailDTO> selectDetailProduct(ProductDTO product);

    // 모든 제품 조회
    List<ProductDTO> selectAllProduct();

    // 새 제품 등록
    void insertProduct(ProductDTO product);

    // 특정 제품 ID의 상세 정보 조회
    ProductDTO selectProductIdDetail(Integer id);

    // 특정 제품 ID의 상세 옵션/세부정보 조회
    List<ProductDetailDTO> selectProductDetail(Integer productId);

    // ----------------------------
    // 랜덤 / 프로모션 제품
    // ----------------------------

    // 랜덤 제품 리스트 조회
    List<ProductDTO> randomProduct();

    // 랜덤 제품 리스트 조회 (별도 버전)
    List<ProductDTO> selectRandomProducts();

    // 랜덤 프로모션 제품 조회
    List<ProductDTO> selectRandomProductPromotion();

    // ----------------------------
    // 페이징 / 통계
    // ----------------------------

    // 조건 기반 제품 리스트 조회 (Map 사용)
    List<Map<String, Object>> list(Map<String, Object> paramMap);

    // 전체 제품 개수 조회 (페이징용)
    int countProducts();

    // ----------------------------
    // 카테고리(Category)
    // ----------------------------

    // 상위 카테고리 목록 조회
    List<CategoryDTO> parentCategory();

    // 특정 상위 카테고리의 하위 카테고리 조회
    List<CategoryDTO> childCategory(int parentId);
}
