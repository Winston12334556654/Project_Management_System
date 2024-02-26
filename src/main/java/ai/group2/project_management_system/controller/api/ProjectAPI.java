package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.mapping.UserMapping;
import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.service.DepartmentService;
import ai.group2.project_management_system.service.Impl.ProjectServiceImpl;
import ai.group2.project_management_system.service.ProjectService;
import ai.group2.project_management_system.service.UserService;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class ProjectAPI {

    private final ProjectService projectService;
    private final DepartmentService departmentService;
    private final UserService userService;

    private final ProjectRepository projectRepository;

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
        System.out.println("member-selector "+users);
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
        List<Project> projects = projectService.getAllProjectsWithUsers();
        System.out.println(projects);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/member")
    public ResponseEntity<List<Long>> getUserIdsByProjectId(@RequestParam Long projectId) {
        System.out.print("Get Mapping"+ projectId);
        List<Long> userIds = projectService.getUserIdsByProjectId(projectId);
        System.out.println(userIds);
        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsersByIds(@RequestParam("userIds") List<Long> userIds) {
        List<User> users = userService.findUsersByIds(userIds);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //These methods are for editing the data
    @GetMapping("/project-selector/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        System.out.println("Start Update"+ projectId);
        Project project = projectService.findById(projectId);
        System.out.println(project);
        if (project != null) {
            return ResponseEntity.ok().body(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user-for-project-update/{departmentId}")
    public ResponseEntity<List<UserDTO>> getUsersForProject(@PathVariable Long departmentId) {
        System.out.println("Updaters are gone");
        List<UserDTO> users = userService.getUsersByDepartmentId(departmentId);
        System.out.println(users);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/show-projects-for-update")
    public ResponseEntity<List<Project>> getAllProjectsForUpdate() {
        List<Project> projects = projectService.getAllProjectsWithUsers();
        System.out.println("All "+ projects);
        return ResponseEntity.ok(projects);
    }


}
