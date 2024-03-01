package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/projects")
    public String project(){
        return "projects";
    }

    //This method is also for selecting back. But I will select with a project's id
    @PostMapping("/project-detail")
    public String projectDetail(@RequestParam("id") Long projectId , Model map){
        System.out.println(projectId);

        Project project = projectService.getProjectBy_Id(projectId);

        System.out.println(project);

        map.addAttribute("project", project);
        return "project-detail";
    }

}












































































































































































































































































































