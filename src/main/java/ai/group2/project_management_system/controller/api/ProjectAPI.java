package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.service.DepartmentService;
import ai.group2.project_management_system.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProjectAPI {

    private final ProjectService projectService;
    private final DepartmentService departmentService;

//    @GetMapping("/department-names")
//    public

    @PostMapping("/add-project")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        System.out.println("Here we go");
        Project newProject = projectService.save(project);
        return ResponseEntity.ok(newProject);
    }
}
