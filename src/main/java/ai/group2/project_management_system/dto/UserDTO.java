package ai.group2.project_management_system.dto;

import ai.group2.project_management_system.model.Enum.Role;
import ai.group2.project_management_system.model.entity.Position;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
    private String profilePictureName;
    private boolean isActive;
    private String password;
    private Position position;
    private Role role;
    private MultipartFile file;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public LocalDate getDob() {
            return dob;
        }

        public void setDob(LocalDate dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProfilePictureName() {
            return profilePictureName;
        }

        public void setProfilePictureName(String profilePictureName) {
            this.profilePictureName = profilePictureName;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public MultipartFile getFile() {
            return file;
        }

        public void setFile(MultipartFile file) {
            this.file = file;
        }
        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", Education='" + education + '\'' +
                    ", dob=" + dob +
                    ", gender='" + gender + '\'' +
                    ", email='" + email + '\'' +
                    ", address='" + address + '\'' +
                    ", phone='" + phone + '\'' +
                    ", position='" + position + '\'' +
                    ", isActive=" +isActive +
                    ", password='" + password + '\'' +
                    ", role=" + role+
                    '}';
        }
    }
