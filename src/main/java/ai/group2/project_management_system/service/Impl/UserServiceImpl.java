package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.mapping.UserMapping;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.UserRepository;
import ai.group2.project_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Override
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);
    }

    @Override
    public List<UserDTO> getUsersByProjectId(Long projectId) {
        List<User> users = userRepository.findByProjects_id(projectId);
        return users.stream()
                .map(userMapping::mapUserToDTOs)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMembersByDepartmentId(Long departmentId) {
        List<User> users = userRepository.findByDepartmentId(departmentId);
        return users;
    }

    @Override
    public String getUserPhotoById(Long id) {
        return userRepository.findPhotoById(id);
    }

    public List<User> getAllUserByIssueId(Long issueId) {
        return userRepository.findAllUserByIssueId(issueId);
    }

    @Override
    public List<User> getUsersByIds(List<Long> teamLeaderIds) {
        return userRepository.findAllById(teamLeaderIds);
    }

    @Override
    public List<Project> getProjectsByUserId(Long userId) {
        return userRepository.findProjectsByUserId(userId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<User> findUsersByIds(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }

    @Override
    public List<UserDTO> getUsersByDepartmentId(Long departmentId) {
        List<User> users = userRepository.findByDepartmentId(departmentId);
        return users.stream()
                .map(userMapping::mapUserToDTOs)
                .collect(Collectors.toList());
    }

    @Override
    public String getUserEmailById(Long userId) {
        return userRepository.findEmailById(userId);
    }

    @Override
    public String getUserNameById(Long userId){
        return userRepository.findUserNameByID(userId);
    }

}
