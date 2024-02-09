package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.DepartmentService;
import ai.group2.project_management_system.service.ProjectService;
import ai.group2.project_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectAPI {

    private final ProjectService projectService;
    private final DepartmentService departmentService;
    private final UserService userService;

    @GetMapping("/departments-selector")
    public ResponseEntity<List<Department>> selectDepartment() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/member-selector")
    public ResponseEntity<List<User>> selectMembers(){
        List<User> users = userService.getAllUsers();
        if(!users.isEmpty()){
            System.out.println(users);
            return ResponseEntity.ok(users);
        }else{
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping("/add-project")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        System.out.println("Here we go");
        Long departmentId = (long) project.getDepartment().getId();
        Project newProject = projectService.save(project);
        return ResponseEntity.ok(newProject);
    }
}
