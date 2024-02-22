package ai.group2.project_management_system.dto;

import ai.group2.project_management_system.model.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String education;
    private LocalDate dob;
    private String gender;
    private String email;
    private String address;
    private String phone;
    private String photo;
    private boolean is_active;
    private String password;
    private String position;
    private Role role;
    private MultipartFile file;



    }
