package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.AssignIssueService;
import ai.group2.project_management_system.service.IssueService;
import ai.group2.project_management_system.service.ProjectService;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class DashboardDataAPI {
    private final ProjectService projectService;
    private final IssueService issueService;
    private final AssignIssueService assignIssueService;
    private final UserService userService;

    public DashboardDataAPI(ProjectService projectService, IssueService issueService, AssignIssueService assignIssueService, UserService userService) {
        this.projectService = projectService;
        this.issueService = issueService;
        this.assignIssueService = assignIssueService;
        this.userService = userService;
    }

    @GetMapping("/projects/count/active")
    public int getActiveProjectCount(){
        return projectService.getActiveProjectCount();
    }

    @GetMapping("/project/count/all")
    public int getProjectCount(){
        return projectService.getProjectCount();
    }

    @GetMapping("/projects/count/inactive")
    public int getInactiveProjectCount(){
        return projectService.getInactiveProjectCount();
    }

    @GetMapping("/issues/count/all")
    public int getIssueCount(){
        return issueService.getIssueCount();
    }

    @GetMapping("/issues/count/active")
    public int getActiveIssuesCount(){
        return issueService.getActiveIssueCount();
    }

    @GetMapping("/issues/count/inactive")
    public int getInactiveIssuesCount(){
        return issueService.getInactiveIssueCount();
    }

    @GetMapping("/assignIssues/count/all")
    public int getAssignIssueCount(){
        return assignIssueService.getAssignIssueCount();
    }

    @GetMapping("/assignIssues/count/active")
    public int getActiveAssignIssuesCount(){
        return assignIssueService.getActiveAssignIssueCount();
    }

    @GetMapping("/assignIssues/count/inactive")
    public int getInactiveAssignIssuesCount(){
        return assignIssueService.getInactiveAssignIssueCount();
    }

    @GetMapping("/find-same-department-with-currentUser")
    public User getCurrentUser(){
        return userService.getCurrentUser();
    }

    @GetMapping("/get-subIssues-by-userId/{userId}")
    public ResponseEntity<List<AssignIssue>> getSubIssuesByUserId(@PathVariable Long userId) {
        System.out.println("Sub Issues are gone");
        List<AssignIssue> subIssues = assignIssueService.getSubIssuesByUserId(userId);
        if (subIssues.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(subIssues);
        }
    }
    @GetMapping("/getUserPhotoById/{assignIssueId}")
    public ResponseEntity<String> getUserPhotoByAssignIssueId(@PathVariable Long assignIssueId) {
        // Step 1: Retrieve the assignIssue based on the provided ID
        AssignIssue assignIssue = assignIssueService.getAssignIssueById(assignIssueId);

        // Step 2: Extract the issue ID from the retrieved assignIssue
        Long issueId = assignIssue.getIssue().getId();

        // Step 3: Retrieve the issue based on the extracted issue ID
        Issue issue = issueService.getIssueById(issueId);

        // Step 4: Retrieve the team leader ID from the retrieved issue
        Long teamLeaderId = issue.getTeamLeaderId();

        // Step 5: Retrieve the user based on the extracted team leader ID
        User user = userService.getUserById(teamLeaderId);

        // Step 6: Fetch the user photo using the retrieved user ID
        String userPhoto = userService.getUserPhotoById(user.getId());

        if (userPhoto != null) {
            return ResponseEntity.ok(userPhoto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getProjectsByUserId/{userId}")
    public ResponseEntity<List<Project>> getProjectIdsByUserId(@PathVariable Long userId) {
        List<Project> projectIds = userService.getProjectsByUserId(userId);
        if (!projectIds.isEmpty()) {
            return ResponseEntity.ok(projectIds);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/projects/count")
    public Map<String, Integer> getProjectCountsByStatus() {
        return projectService.getCountsByStatus();
    }
    @GetMapping("/issues/count")
    public Map<String, Integer> getIssuesCountsByStatus() {
        return issueService.getCountsByStatus();
    }




}
