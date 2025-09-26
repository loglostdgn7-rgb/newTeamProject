package team.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import team.project.service.ProductService;

@ControllerAdvice
public class PublicController {
    @Autowired
    private ProductService productService;

    @ModelAttribute
    public void category(Model model) {
        model.addAttribute("ParentCategory", productService.getParentCategoryMap());
        model.addAttribute("ChildCategory", productService.getChildCategoryMap());
    }
}