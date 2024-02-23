package ai.group2.project_management_system.service;

import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.model.entity.User;

import java.util.List;


import java.util.Set;



public interface UserService {
    User save (User user);
    List<User> getAllUsers();


    User getUserById(long userId);

    List<User> findUsersByIds(List<Long> userIds);
    List<UserDTO> getUsersByDepartmentId(Long departmentId);
    User getCurrentUser();
    List<UserDTO> getUsersByProjectId(Long projectId);


}
