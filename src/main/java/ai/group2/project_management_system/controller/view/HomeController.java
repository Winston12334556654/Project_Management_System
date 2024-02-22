package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.model.Enum.Role;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final UserService userService;
    @GetMapping("/home")
    public String home(HttpSession session){
        var user = userService.getCurrentUser();
        session.setAttribute("id",user.getId());
        session.setAttribute("role",user.getRole());
        System.out.println("UserId:"+user.getId());
        System.out.println("UserName:"+user.getName());
        return "index";
    }


}
