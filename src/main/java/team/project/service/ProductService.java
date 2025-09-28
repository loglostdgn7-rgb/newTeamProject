package team.project.service;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import team.project.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.mapper.ProductMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired ProductMapper productMapper;



    public List<ReviewDTO> getReview(){
        return productMapper.getReview();
    }



    // Header 대분류
    @Getter
    Map<Integer,CategoryDTO> parentCategoryMap = new HashMap<>();
    // Header 소분류
    @Getter Map<Integer,List<CategoryDTO>> childCategoryMap = new HashMap<>();

    @PostConstruct
    public void Category(){
        List<CategoryDTO> parentCategory = productMapper.parentCategory();
        System.out.println(parentCategory);
        for(CategoryDTO ParentCategory : parentCategory){
            parentCategoryMap.put(ParentCategory.getCategoryId(), ParentCategory);
            List<CategoryDTO> childCategory = productMapper.childCategory(ParentCategory.getCategoryId());
            childCategoryMap.put(ParentCategory.getCategoryId(), childCategory);
        }
    }

        public Map<Integer, CategoryDTO> getParentCategoryMap() {
            return parentCategoryMap;
        }

        public Map<Integer, List<CategoryDTO>> getChildCategoryMap() {
            return childCategoryMap;
        }


    // index에서 신상품 목록에 랜덤으로 10개 가져오는 메서드
    public void randomProducts(PagenationDTO pagenation) {
        List<ProductDTO> products = productMapper.selectRandomProducts();
        pagenation.setElements(products);
    }



    public void add_product(ProductDTO productDTO) {
        productMapper.insertProduct(productDTO);
    }

    public ProductDTO get_id_product_detail(Integer id) {
        return productMapper.selectProductIdDetail(id);
    }

    public List<ProductDetailDTO> get_product_detail(Integer productId) {
        return productMapper.selectProductDetail(productId);
    }

    public void get_details(ProductDTO product){
        List<ProductDetailDTO> details = productMapper.selectDetailProduct(product);
        product.setElements(details);
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
    }    // 카테고리별 상품을 가져오는 기능
    public void get_productsCategory( int parentId, PagenationDTO pagenation) {
        List<ProductDTO> elements = productMapper.selectProductsCategory( parentId, pagenation);
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
