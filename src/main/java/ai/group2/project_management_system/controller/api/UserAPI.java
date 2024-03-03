package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.Enum.Role;
import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Position;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.*;
import ai.group2.project_management_system.service.Impl.PositionServiceImpl;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserAPI {
    private final UserService userService;

    @Autowired
    private PositionService positionService;
    @Autowired
    private ImageService imageService;


    @Value("${upload.directory}")
    private String uploadDir;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createUser(
            @RequestParam("name") String name,
            @RequestParam("department") Department department,
            @RequestParam("position") Position position,
            @RequestParam("gender") String gender,
            @RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob, @RequestParam("education") String education,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("password") String password,
            @RequestParam("file") MultipartFile file) {
        try {

            System.out.println("Department = "+ department);
            System.out.println("Position = "+ position);
            MultipartFile photo = file;
            System.out.println("photo : " + photo);
            if (photo != null && !photo.isEmpty()) {

                String savedImagePath = imageService.saveImageAsync(photo);
                if (savedImagePath != null && (savedImagePath.endsWith(".jpg") || savedImagePath.endsWith(".jpeg") ||
                        savedImagePath.endsWith(".png") || savedImagePath.endsWith(".gif"))) {
                    String image = savedImagePath;
                    User user = new User();
                    user.setName(name);
                    user.setDepartment(department);
                    user.setPosition(position);
                    user.setRole(Role.PM);
                    user.setGender(gender);
                    user.setDob(dob);
                    user.setEducation(education);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setActive(true);
                    user.setAddress(address);
                    user.setPassword(password);
                    user.setProfilePictureFileName(image);
                    userService.save(user);
                } else {
                    System.err.println("Upload file is not an allowed image type.");
                }

            }
            // Response returning message if creation was successful
            Map<String, String> response = new HashMap<>();
            response.put("message", "User created successfully");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();

            // Response returning message if creation was failed
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to create user");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/positions")
    public ResponseEntity<List<Position>> getPositions() {
        List<Position> positions = positionService.getAllPositions();

        return ResponseEntity.ok(positions);
    }

    @GetMapping("/get-lists")
    public ResponseEntity<List<User>> getUserList() {
        List<User> users = userService.getAllUsers();
        System.out.println("All Users: " + users); // Log the users before sending the response
        users.forEach(user -> {
            System.out.println("User isActive: " + user.isActive());
        });
        return ResponseEntity.ok(users);
    }


    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<User> updateStatus(@PathVariable("id") long userId) {
        User user = userService.getUserById(userId);
        System.out.println("USer Id : "+ userId);
        System.out.println("USer former status : "+ user.isActive());

        if (user != null) {
            user.setActive(!user.isActive()); // Toggle the status
            User updatedUser = userService.save(user);
            System.out.println("USer later status : "+ user.isActive());
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserWithImage(
            @PathVariable("id") long userId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("password") String password,
            @RequestParam("department") Department department,
            @RequestParam("position") Position position,
            @RequestParam("gender") String gender,
            @RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob,
            @RequestParam("education") String education,
            @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Received update request for user ID: " + userId);
            System.out.println("File Name : " + file);

            // Retrieve the existing user from the database
            User existingUser = userService.getUserById(userId);

            if (existingUser != null) {
                // Update the existing user with the new information
                existingUser.setName(name);
                existingUser.setEmail(email);
                existingUser.setPhone(phone);
                existingUser.setAddress(address);
                existingUser.setPassword(password);
                existingUser.setDepartment(department);
                existingUser.setPosition(position);
                existingUser.setGender(gender);
                existingUser.setDob(dob);
                existingUser.setEducation(education);
                // Update other fields as needed

                // Process the image file
                MultipartFile photo = file;
                System.out.println("photo : "+photo);
                if (photo != null && !photo.isEmpty()) {
                    String savedImagePath = imageService.saveImageAsync(photo);
                    System.out.println("savedImagePath  : "+ savedImagePath);
                    if (savedImagePath != null && (savedImagePath.endsWith(".jpg") || savedImagePath.endsWith(".jpeg") ||
                            savedImagePath.endsWith(".png") || savedImagePath.endsWith(".gif"))) {
                        existingUser.setProfilePictureFileName(savedImagePath);
                    } else {
                        System.err.println("Upload file is not an allowed image type.");
                    }
                }

                // Save the updated user to the database
                User savedUser = userService.save(existingUser);

                return ResponseEntity.ok(savedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();

            // Return an error response if the update fails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(){
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser); // This will return the authenticated user
    }
}
