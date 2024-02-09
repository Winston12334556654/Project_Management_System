package ai.group2.project_management_system.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Department department;
    private String education;
    private Date dob;
    private String gender;
    private String email;
    private String address;
    private String phone;
    private String photo;
    private boolean status;
    private String password;
    private String position;
    private String role;
    @Transient
    private MultipartFile file;

    @ManyToMany(mappedBy = "users")
    private Set<Project> projects;


    @OneToMany(mappedBy = "user")
    private Set<AssignIssue> assignIssues;



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + department +
                ", Education='" + education + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", status=" + status +
                ", password='" + password + '\'' +
                ", file=" + file +
                '}';
    }
}
