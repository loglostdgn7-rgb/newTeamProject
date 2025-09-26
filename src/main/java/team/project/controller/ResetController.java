package team.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import team.project.dto.UserDTO;
import team.project.service.ResetService;
import team.project.service.user.UserService;

@Controller
public class ResetController {
    Logger logger = LoggerFactory.getLogger(ResetController.class);

    @Autowired
    ResetService resetService;
    @Autowired
    private UserService userService;
    
    @PostMapping("/admin/reset-all")
    public ResponseEntity<String> post_reset_all(Model model, String id) {
        try {
            UserDTO isAdminUser = userService.get_user_by_Id(id);
            System.out.println(isAdminUser);

            int updatedUserCount = resetService.reset_all_test_users();
            String message = updatedUserCount + "명의 테스트 유저 정보가 성공적으로 초기화 되었습니다.";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            logger.error("관리자에 의한 수동 초기화 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("초기화 중 서버 오류가 발생했습니다.");
        }
    }
}


