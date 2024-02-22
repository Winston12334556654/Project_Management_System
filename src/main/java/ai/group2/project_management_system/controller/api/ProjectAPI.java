package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.mapping.UserMapping;
import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.DepartmentService;
import ai.group2.project_management_system.service.ProjectService;
import ai.group2.project_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectAPI {

    private final ProjectService projectService;
    private final DepartmentService departmentService;
    private final UserService userService;

    private final UserMapping userMapping;

    @GetMapping("/departments-selector")
    public ResponseEntity<List<Department>> selectDepartment() {
        System.out.println("department is gone");
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/members-selector/{departmentId}")
    public ResponseEntity<List<UserDTO>> getUsers(@PathVariable Long departmentId) {
        System.out.println("Users are gone");
        List<UserDTO> users = userService.getUsersByDepartmentId(departmentId);

        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    // This method is for creating new projects
    @PostMapping("/add-project")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {

        System.out.println("Here we go");

        project.setIsActive(true);

        System.out.println(project);
        List<Long> userIds = project.getUserIds();
        List<User> users = userService.findUsersByIds(userIds);
        project.setUsers(new HashSet<>(users));

        Project newProject = projectService.save(project);
        return ResponseEntity.ok(newProject);
    }

    //These methods are for selecting back and display back the project list
    @GetMapping("/show-projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/user-getter")
    public ResponseEntity<List<User>> getUserById(@RequestParam("userIds") List<Long> userId) {
        List<User> user = userService.findUsersByIds(userId);
        if (user != null) {
            System.out.println(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //These methods are for editing the data











}
