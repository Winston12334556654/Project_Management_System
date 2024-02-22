package ai.group2.project_management_system.mapping;

import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapping {

    public UserDTO mapUserToDTOs(User users){
        UserDTO dto = new UserDTO();
        dto.setId(users.getId());
        dto.setName(users.getName());
        dto.setEducation(users.getEducation());
        dto.setDob(users.getDob());
        dto.setGender(users.getGender());
        dto.setEmail(users.getEmail());
        dto.setAddress(users.getAddress());
        dto.setPhone(users.getPhone());
        dto.setPhoto(users.getPhoto());
        dto.setPassword(users.getPassword());
        dto.setRole(users.getRole());
        dto.setPosition(users.getPosition());

        return dto;
    }


}
