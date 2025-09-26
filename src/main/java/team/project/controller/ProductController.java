package team.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import team.project.dto.pagenationDTO;
import team.project.dto.ProductDTO;
import team.project.dto.ProductDetailDTO;
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
            pagenationDTO pagenation
    ) {
        productService.get_products(pagenation);

        if (pagenation.getElements() != null) {
            for (Object element : pagenation.getElements()) {
                if (element instanceof ProductDTO) {
                    ProductDTO product = (ProductDTO) element;
                    if (product.getImageData() != null && product.getImageData().length > 0) {
                        String base64Image = Base64.getEncoder().encodeToString(product.getImageData());
                        product.setBase64ImageData(base64Image); }
                }
            }
        }

        model.addAttribute("pagenation", pagenation);

        System.out.println("pagenation: " + pagenation);
    }





    @GetMapping("/product/detail/{id}")
    public String get_detail(
            Model model,
            //////// Integer -> int로 [김영수]님이 수정(9/19) ///////////
            @PathVariable("id") int id,
            ProductDTO product
    ){
        productService.get_details(product);

        for(Object elements: product.getElements()){
            ProductDetailDTO productDetail = (ProductDetailDTO) elements;
            if(productDetail.getDetailImageData() != null && productDetail.getDetailImageData().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(productDetail.getDetailImageData());
                productDetail.setBaseDetailImageData(base64Image);
            }
        }
            var productDetails =  productService.get_id_product_detail(id);
        model.addAttribute("productDetails", productDetails);
        model.addAttribute("product", product);
        return "shop/product/detail";
    }







    @GetMapping("/product/set")
    public String get_product_set() {
        return "shop/product/product_set";
    }
}
