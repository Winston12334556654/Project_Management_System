package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.service.Impl.DepartmentServiceImpl;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Autowired
    DepartmentServiceImpl departmentService;


    @GetMapping("/user-management")
    public String getAllUsers(User user) {

        return "user-management";
    }

}
