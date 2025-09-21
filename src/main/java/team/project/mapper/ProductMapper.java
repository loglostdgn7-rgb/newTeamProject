package team.project.mapper;

import org.apache.ibatis.annotations.Param;
import team.project.dto.PagenationDTO;
import team.project.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import team.project.dto.ProductDetailDTO;

import java.util.List;
import java.util.Map;


@Mapper
public interface ProductMapper {
    List<ProductDTO> selectProducts(PagenationDTO pagenation);
    List<ProductDetailDTO> selectDetailProduct(ProductDTO product);

    List<ProductDTO> selectAllProduct();

    void insertProduct(ProductDTO product);
    ProductDTO selectProductIdDetail(Integer id);
    List<ProductDetailDTO> selectProductDetail(Integer productId);

    List<Map<String, Object>> list (Map<String, Object> paramMap);
    int countProducts();
}
