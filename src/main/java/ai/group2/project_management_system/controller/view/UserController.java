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
    public String getAllUsers(ModelMap model) {
        // Retrieve all departments
//        List<User> users = userService.getAllUsers();
//        List<Position> positions = positionService.getAllPositions();
//        List<Department> departments = departmentService.getAllDepartments();
//
//        model.addAttribute("users", users);
//        model.addAttribute("user",new User());
//        model.addAttribute("positions", positions);
//        model.addAttribute("departments", departments);

        // Return the Thymeleaf template name
        return "user-management";
    }

//    @PostMapping(value = "/user-management/add-user", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    @CrossOrigin
//    public String addUser(@RequestBody User user){
//        System.out.println(user.getName()+" "+user.getPosition());
//        userService.addUser(user);
//        return "successful";
//    }

    //private static final String UPLOAD_DIRECTORY ="/home/tinhtet/Documents/OJT Batch 11 (Ace Inspiration)/Project/ProjectManagementSystem/src/main/resources/static/assets/images/userPhoto";

//    @PostMapping(value = "/user-management/add-user")
//    public String addUser(@ModelAttribute User user,@RequestParam("file") MultipartFile file) throws IOException {
//        System.out.println(user.getName());
////        Date dob = new Date();
////        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
////        String formattedDate = sdf.format(dob);
//        String photoname =file.getOriginalFilename();
//
//        user.setPhoto(photoname);
//        user.setStatus(true);
//        userService.addUser(user);
//        byte[] bytes = file.getBytes();
//        BufferedOutputStream stream = new BufferedOutputStream(
//                new FileOutputStream(new File(UPLOAD_DIRECTORY + File.separator + photoname)));
//        stream.write(bytes);
//        stream.flush();
//        stream.close();
//        return "redirect:/admin/user-management";
//    }

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
            // Convert departmentName and positionName to Department and Position objects
            //Department department = departmentService.getDepartmentByName(departmentName);
           // Position position = positionService.getPositionByName(positionName);

            // Set the Department and Position objects in the User
            //user.setDepartment(department);
            //user.setPosition(position);
            // Set photo name in the User object
            user.setPhoto(photoname);
            user.set_active(true);
            System.out.println(user.getName());

            // Add user (consider transaction management)
            userService.save(user);

            return "redirect:/admin/user-management";

    }
}
