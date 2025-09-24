package team.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import team.project.dto.PaginationDTO;
import team.project.dto.ProductDTO;
import team.project.service.ProductService;

import java.util.Base64;

@Controller
public class MainController {
    @Autowired ProductService productService;
// 해당 경로로 들어가기 위해 그냥 만들어둔 "그냥 컨트롤러"

    @GetMapping("/")
    public String getIndex(
            Model model,
            PaginationDTO pagenation
    ) {
        productService.randomProducts(pagenation);

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

        return "index"; // 뷰 이름 반환
    }

//    회사소개
    @GetMapping("/intro/about")
    public void get_about() {
    }


//    Q&A
    @GetMapping("/board/qna")
    public void get_qna(){
    }


}
