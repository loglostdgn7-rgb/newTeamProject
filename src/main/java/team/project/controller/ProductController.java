package team.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.project.dto.PagenationDTO;
import team.project.dto.ProductDTO;
import team.project.dto.ProductDetailDTO;
import team.project.dto.*;
import team.project.service.ProductService;
import team.project.util.ImageUtils;

import java.util.Base64;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/shop")
public class ProductController {
    @Autowired
    ProductService productService;

    //    @GetMapping("/product/list/{parentId}")
//    public void get_list(
//            Model model,
//            PagenationDTO pagenation
//    ) {
//        productService.get_products(pagenation);
//
//
//        if (pagenation.getElements() != null) {
//            for (Object element : pagenation.getElements()) {
//                if (element instanceof ProductDTO) {
//                    ProductDTO product = (ProductDTO) element;
//                    if (product.getImageData() != null && product.getImageData().length > 0) {
//                        String base64Image = Base64.getEncoder().encodeToString(product.getImageData());
//                        product.setBase64ImageData(base64Image); }
//                }
//            }
//        }
//
//        model.addAttribute("pagenation", pagenation);
//
//        System.out.println("pagenation: " + pagenation);
//    }
    @GetMapping("/product/list/{parentId}")
    public String get_list(
            Model model,
            @PathVariable("parentId") int parentId,
            PagenationDTO pagenation
    ) {
        // 해당 카테고리 상품 가져오기
        productService.get_productsCategory(parentId, pagenation);

        // 이미지 Base64 변환
        if (pagenation.getElements() != null) {
            for (Object element : pagenation.getElements()) {
                if (element instanceof ProductDTO product) {
                    if (product.getImageData() != null && product.getImageData().length > 0) {
                        String base64Image = Base64.getEncoder().encodeToString(product.getImageData());
                        product.setBase64ImageData(base64Image);
                    }
                }
            }
        }

        // 페이지네이션과 parentId를 모델에 담기
        model.addAttribute("pagenation", pagenation);
        model.addAttribute("parentId", parentId);
        System.out.println(pagenation);
        System.out.println(parentId);

        // Thymeleaf 템플릿 경로 반환
        return "shop/product/list";
    }


    ////////  김영수님이 추가.9/28 ///////////
    @GetMapping("/product/{search}")
    public String get_product_search(
            @RequestParam("searchValue") String searchValue,
            PagenationDTO pagenation,
            Model model
    ) {
        pagenation.setSearchValue(searchValue);
        productService.search_products(pagenation);

        if (pagenation.getElements() != null) {
            for (Object element : pagenation.getElements()) {
                if (element instanceof ProductDTO) {
                    ProductDTO product = (ProductDTO) element;
                    if (product.getImageData() != null && product.getImageData().length > 0) {
                        String base64Image = ImageUtils.imageDataUri(product.getImageData(), "image/jpeg");
                        product.setBase64ImageData(base64Image);
                    }
                }
            }
        }

        model.addAttribute("pagenation", pagenation);

        return "search_result";
    }





    @GetMapping("/product/detail/{id}")
    public String get_detail(
            Model model,
            //////// Integer -> int로 [김영수]님이 수정(9/19) ///////////
            @PathVariable("id") int id,
            ProductDTO product
    ) {
        productService.get_details(product);

        for (Object elements : product.getElements()) {
            ProductDetailDTO productDetail = (ProductDetailDTO) elements;
            if (productDetail.getDetailImageData() != null && productDetail.getDetailImageData().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(productDetail.getDetailImageData());
                productDetail.setBaseDetailImageData(base64Image);
            }
        }
        var productDetails = productService.get_id_product_detail(id);
        model.addAttribute("productDetails", productDetails);
        model.addAttribute("product", product);
        return "shop/product/detail";
    }







    @GetMapping("/product/set")
    public String get_product_set() {
        return "shop/product/product_set";
    }
}
