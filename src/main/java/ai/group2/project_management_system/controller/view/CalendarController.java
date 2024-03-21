package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
@Controller
@RequiredArgsConstructor
@Slf4j
public class CalendarController {
    private final UserService userService;

    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session) {
        User user = userService.getCurrentUser();
        return user;
    }
    @GetMapping("/calendar")
    public String getCalendar(){

        return "calendar";
    }
}
