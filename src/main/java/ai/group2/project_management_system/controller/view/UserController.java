package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/user-management")
    public String getAllUsers(User user) {

        return "user-management";
    }

}
