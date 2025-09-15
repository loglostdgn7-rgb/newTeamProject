package team.project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
// 해당 경로로 들어가기 위해 그냥 만들어둔 "그냥 컨트롤러"

    @GetMapping("/")
    public String index() {
        return "index";
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
