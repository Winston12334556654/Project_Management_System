package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.ImageService;
import ai.group2.project_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ProfileAPI {
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/user/profile")
    public ResponseEntity<User> getCurrentUser() {
       User currentUser = userService.getCurrentUser();
       System.out.println(currentUser);
        return ResponseEntity.ok(currentUser);
    }


    @GetMapping("/user/password/{password}")
    public boolean getCurrentUserPassword(@PathVariable("password") String password) {
        User currentUser = userService.getCurrentUser();
        boolean checkedPassword = BCrypt.checkpw(password, currentUser.getPassword());
        return checkedPassword;
    }

    @PostMapping("/edit-user/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") Long userId, @RequestBody User user){
        System.out.println("we reach edit mapping!");

        User user1 = userService.getUserById(userId);
        if (user1 != null) {

            user1.setName(user.getName());
            user1.setAddress(user.getAddress());
            user1.setDob(user.getDob());
            user1.setEducation(user.getEducation());
            user1.setEmail(user.getEmail());
            user1.setGender(user.getGender());
            user1.setPhone(user.getPhone());
            user1.setDepartment(user.getDepartment());
            user1.setPosition(user.getPosition());
            //user1.setProfilePictureFileName(user.getProfilePictureFileName());
            User latestUser = userService.save(user1);
            return ResponseEntity.ok(latestUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/edit-profilePhoto/{id}")
    public ResponseEntity<User> updateUserProfilePhoto(@PathVariable("id") long userId,@RequestParam("file") MultipartFile file){
        try {
            System.out.println("Received update request for user ID: " + userId);
            System.out.println("File Name : " + file);

            // Retrieve the existing user from the database
            User existingUser = userService.getUserById(userId);
            if(existingUser!=null){
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
                }// Save the updated user to the database
                User savedUser = userService.save(existingUser);

                return ResponseEntity.ok(savedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
            // Return an error response if the update fails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/update-password/{userId}")
    public ResponseEntity<User> updatePassword(@PathVariable("userId") long userId, @RequestBody User user) {
        System.out.println("we reach edit mapping!");

        User existingUser = userService.getUserById(userId);
        if (existingUser != null) {
            // Update the password
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            existingUser.setPassword(hashedPassword);
            User updatedUser = userService.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
