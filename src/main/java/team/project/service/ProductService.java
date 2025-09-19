package team.project.service;
import org.springframework.data.domain.Page;
import team.project.dto.PagenationDTO;
import team.project.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.mapper.ProductMapper;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired ProductMapper productMapper;


    public void add_product(ProductDTO productDTO) {
        productMapper.insertProduct(productDTO);
    }

    public ProductDTO get_id_product_detail(Integer id) {
        return productMapper.selectProductIdDetail(id);
    }

    // 모든 상품을 가져오는 기능
    public void get_products(PagenationDTO pagenation) {
        List<ProductDTO> elements = productMapper.selectProducts(pagenation);
        // 화면에 표시할 요소가 있다면
        if(elements != null && !elements.isEmpty()) {
            Integer totalElementCount = productMapper.countProducts();
            pagenation.setTotalElementsCount(totalElementCount);
            // 페이지네이션 객체가 실제로 화면에 표시할 데이터들을 가지게 한다
            pagenation.setElements(elements);
        }
        // 화면에 표시할 요소가 없다면
        else{
            pagenation.setTotalElementsCount(0);
            pagenation.setElements(List.of());
        }
    }
}
