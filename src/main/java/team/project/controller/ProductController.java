package team.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import team.project.dto.PagenationDTO;
import team.project.dto.ProductDTO;
import team.project.service.ProductService;

import java.util.Base64;

@Slf4j
@Controller
@RequestMapping("/shop")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/product/list")
    public void get_list(
            Model model,
            PagenationDTO pagenation
    ) {
        // productService에서 pagenation 객체를 업데이트하고 productDTO 리스트를 가져온다고 가정
        productService.get_products(pagenation); // 이 안에서 productDTO 리스트가 pagenation에 채워질 거야.

        // ✨ 추가할 부분 시작! ✨
        if (pagenation.getElements() != null) {
            // pagenation.elements에 있는 각 ProductDTO를 반복하며 이미지 처리
            for (Object element : pagenation.getElements()) {
                if (element instanceof ProductDTO) { // 안전하게 형변환
                    ProductDTO product = (ProductDTO) element;
                    if (product.getImageData() != null && product.getImageData().length > 0) {
                        String base64Image = Base64.getEncoder().encodeToString(product.getImageData());
                        product.setBase64ImageData(base64Image); // DTO에 base64 문자열을 저장
                    }
                }
            }
        }
        // ✨ 추가할 부분 끝! ✨

        model.addAttribute("pagenation", pagenation);

        System.out.println("pagenation: " + pagenation);
    }


    @GetMapping("/product/detail/{id}")
    public String get_detail(
            Model model,
            //////// Integer -> int로 [김영수]님이 수정(9/19) ///////////
            @PathVariable("id") int id
    ){
        var productDetail = productService.get_id_product_detail(id);

        // BLOB → Base64 변환
        if (productDetail.getImageData() != null && productDetail.getImageData().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(productDetail.getImageData());
            model.addAttribute("base64Image", base64Image);
        }

        model.addAttribute("productDetail", productDetail);
        return "shop/product/detail";
    }







    @GetMapping("/product/set")
    public String get_product_set() {
        return "shop/product/product_set";
    }
}
