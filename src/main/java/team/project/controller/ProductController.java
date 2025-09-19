package team.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import team.project.dto.PagenationDTO;
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
        productService.get_products(pagenation);
        model.addAttribute("pagenation", pagenation);

        System.out.println("pagenation: " + pagenation);
    }


    @GetMapping("/product/detail/{id}")
    public String get_detail(
            Model model,
            @PathVariable("id") Integer id
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
