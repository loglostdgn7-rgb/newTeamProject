package team.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import team.project.dto.MainImageDTO;
import team.project.dto.PagenationDTO;
import team.project.dto.ProductDTO;
import team.project.mapper.MainImageMapper;
import team.project.service.ProductService;
import team.project.util.ImageUtils;

import java.util.Base64;
import java.util.Objects;

@Controller
public class MainController {
    @Autowired
    ProductService productService;
    @Autowired
    private MainImageMapper mainImageMapper;
    // 해당 경로로 들어가기 위해 그냥 만들어둔 "그냥 컨트롤러"


    @GetMapping("/")
    public String getIndex(
            Model model,
            PagenationDTO pagenation
    ) {

        MainImageDTO randomMainImage = mainImageMapper.selectRandomMainImage();

        if (Objects.nonNull(randomMainImage) && Objects.nonNull(randomMainImage.getImageData())) {
            String mainImageUri = ImageUtils.imageDataUri(randomMainImage.getImageData(), "image/jpeg");
            randomMainImage.setBase64Image(mainImageUri);
            model.addAttribute("randomMainImage", randomMainImage);
        }

        productService.randomProducts(pagenation);
/// //////////////////////////////////////////////////////////////////////

        if (pagenation.getElements() != null) {
            for (Object element : pagenation.getElements()) {
                if (element instanceof ProductDTO) {
                    ProductDTO product = (ProductDTO) element;
                    if (product.getImageData() != null && product.getImageData().length > 0) {
//                        String base64Image = Base64.getEncoder().encodeToString(product.getImageData());
//                        product.setBase64ImageData(base64Image);
                        /***김영수님 9/27일 수정. 위에서 아래껄로***/
                        String productUri = ImageUtils.imageDataUri(product.getImageData(), "image/jpeg");
                        product.setBase64ImageData(productUri);
                    }
                }
            }
        }
        model.addAttribute("pagenation", pagenation);

        return "index"; // 뷰 이름 반환
    }
/// //////////////////////////////////////////////////////////////////////////////////

    //    회사소개
    @GetMapping("/intro/about")
    public void get_about() {
    }


    //    Q&A
    @GetMapping("/board/qna")
    public void get_qna() {
    }


}
