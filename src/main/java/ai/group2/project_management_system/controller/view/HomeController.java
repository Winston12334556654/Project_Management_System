package ai.group2.project_management_system.controller.view;


import ai.group2.project_management_system.model.Enum.Role;
import ai.group2.project_management_system.model.entity.*;
import ai.group2.project_management_system.repository.AssignIssueRepository;
import ai.group2.project_management_system.service.AssignIssueService;
import ai.group2.project_management_system.service.IssueService;
import ai.group2.project_management_system.service.ProjectService;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final UserService userService;
    private final ProjectService projectService;
    private final IssueService issueService;
    private final AssignIssueService assignIssueService;
    private final AssignIssueRepository assignIssueRepository;
    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session) {
        User user = userService.getCurrentUser();
        return user;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        var user = userService.getCurrentUser();

        System.out.println("Current User: " + user);

        if (user.getRole() != Role.PM || user.getRole() != Role.PMO) {
            Department department = user.getDepartment();
            if (department != null) {
                session.setAttribute("departmentId", department.getId());
            } else {
                System.out.println("There is no department!");
            }
        }
//        session.setAttribute("user", user);
        List<Project> activeProjects=projectService.getActiveProjects();
        List<Issue> issues=issueService.getAllIssues();
        List<Issue> activeIssues = issues.stream()
                .filter(Issue::isActive)
                .collect(Collectors.toList());

        List<AssignIssue> assignIssues=assignIssueRepository.findAll();
        List<AssignIssue> activeAssignIssues=assignIssues.stream()
                .filter(AssignIssue::isActive)
                        .collect(Collectors.toList());


       // System.out.println("TeamLeaders:"+teamLeaders.size());
        model.addAttribute("activeProjects",activeProjects);
        model.addAttribute("activeIssues",activeIssues);
        model.addAttribute("activeAssignIssues",activeAssignIssues);
        return "index";
    }


    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }

}