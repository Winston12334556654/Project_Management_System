package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.model.Enum.Role;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class UserController {
private final UserService userService;
    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session) {
        User user = userService.getCurrentUser();
        return user;
    }

    @GetMapping("/user-management")
    public String getAllUsers(User user) {

        return "user-management";
    }

    @PostMapping(value = "/user-management/add-user")
    public String addUser(@ModelAttribute("user") User user) {

            MultipartFile file = user.getFile();
            String photoname = file.getOriginalFilename();
            String uploadPath = "/home/tinhtet/Documents/OJT Batch 11 (Ace Inspiration)/Project/ProjectManagementSystem/src/main/resources/static/assets/images/userPhoto/"
                    + photoname;
            try {
                FileOutputStream fos = new FileOutputStream(uploadPath);
                InputStream is = file.getInputStream();
                byte[] data = new byte[is.available()];
                is.read(data);
                fos.write(data);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/error";
            }
            user.setProfilePictureFileName(photoname);
            user.setRole(Role.MEMBER);
            user.setActive(true);
            System.out.println(user.getName());

            userService.save(user);

            return "redirect:/admin/user-management";
    }
}
