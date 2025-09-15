package team.project.mapper;

import org.apache.ibatis.annotations.Param;
import team.project.dto.PagenationDTO;
import team.project.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductMapper {
    List<ProductDTO> selectProducts(PagenationDTO pagenation);

    List<ProductDTO> selectAllProduct();

    void insertProduct(ProductDTO product);
    ProductDTO selectProductIdDetail(Integer id);


}
