package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.AssignIssueRepository;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.service.ProjectService;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController {
    private final IssueRepository issueRepository;
    private final AssignIssueRepository assignIssueRepository;
    private final ProjectRepository projectRepository;
   private final ProjectService projectService;
    private final UserService userService;
    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session) {
        User user = userService.getCurrentUser();
        return user;
    }

    @GetMapping("/projects")
    public String project(){
        return "projects";
    }

    //This method is also for selecting back. But I will select with a project's id
    @GetMapping("/project-detail/{id}")
    public String projectDetail(@PathVariable("id") Long projectId , Model model){
       // System.out.println(projectId);
        Project project = projectService.getProjectBy_Id(projectId);
        var user = userService.getCurrentUser();
        List<Issue> issues=issueRepository.findIssuesByProjectId(projectId);

        model.addAttribute("issues",issues);
        model.addAttribute("project", project);
        return "project-detail";
    }

    @GetMapping("/project-issue-detail/{id}")
    public String projectIssuesDetail(@PathVariable("id") Long issueId , Model model){
        Issue issue=issueRepository.getIssueById(issueId);
        var user = userService.getCurrentUser();
        List<AssignIssue> assignIssues=assignIssueRepository.findAssignIssuesByIssueId(issueId);
        System.out.println("Assign Issues:"+assignIssues.size());
        model.addAttribute("assignIssues",assignIssues);
        model.addAttribute("issue", issue);
        return "project-issue-detail";
    }

    @GetMapping("/my-projects")
    public String myProjects(Model model){
        List<Project> projects=projectRepository.findProjectsByCreator(userService.getCurrentUser().getName());
        model.addAttribute("projects",projects);
        return "my-projects";
    }

}












































































































































































































































































































