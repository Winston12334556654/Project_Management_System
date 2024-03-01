package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.dto.IssueDetailsDto;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.service.AssignIssueService;
import ai.group2.project_management_system.service.IssueService;
import ai.group2.project_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IssueController {
    private final UserService userService;
    private final IssueService issueService;
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    @GetMapping("/issue-list")
    public String issueList(Model model){
        var user = userService.getCurrentUser();
        List<Issue> pmIssues=issueRepository.getIssuesByCreator(user.getName());

    //    log.info("Issue -> {}",pmIssues.size());
        model.addAttribute("pmIssues",pmIssues);
        return "issue-list";
    }

    @GetMapping("/issueboard")
    public String issueBoard(Model model){
        var user=userService.getCurrentUser();
        List<Project> creatorProjects=projectRepository.findProjectsByCreator(user.getName());
        List<Project> currentProjects=new ArrayList<Project>();
        for (int i = 0; i < creatorProjects.size(); i++) {
            if (!creatorProjects.get(i).getStatus().equals(Status.COMPLETED)) {
                currentProjects.add(creatorProjects.get(i));
            }
        }

        List<Issue> creatorIssues=issueRepository.getIssuesByCreator(user.getName());
        List<Issue> currentIssues= new ArrayList<Issue>();
        for (int i = 0; i < creatorIssues.size(); i++) {
            if (!creatorIssues.get(i).getProject().getStatus().equals(Status.COMPLETED)) {
                currentIssues.add(creatorIssues.get(i));
            }
        }

        for(Project currentProject:currentProjects){
            boolean anyInProgress = false;
            boolean allCompleted = true;
            boolean issuesFound = false;
            for(Issue issue:currentIssues) {

                if (currentProject.getTitle().equals(issue.getProject().getTitle())) {
                    issuesFound = true;
                    if (issue.getStatus() == Status.INPROGRESS) {
                        anyInProgress = true;
                        break; // Break if any assign issue is in progress
                    } else if (issue.getStatus() != Status.COMPLETED && issue.getStatus() != null) {
                        allCompleted = false; // Not all assign issues are completed
                    }
                }
            }

            // Check if no issues were found for the project
            if (!issuesFound) {
                currentProject.setStatus(Status.TODO);
            } else {
                if (anyInProgress) {
                    currentProject.setStatus(Status.INPROGRESS);
                    currentProject.setActualStartDate(LocalDate.now());
                } else if (allCompleted) {
                    currentProject.setStatus(Status.PENDING);
                }
            }

            projectRepository.save(currentProject);
        }

//        log.info(" All Project list -> {}",creatorIssues.size());
//          log.info("Project list -> {}",currentIssues.size());
        model.addAttribute("issues",currentIssues);
        model.addAttribute("projects",currentProjects);
        return "issueboard";
    }

    @GetMapping("/all-issue-list")
    public String allIssueList(){
        return "all-issue-list";
    }

    @GetMapping("/teamleader-progress-view")
    public String teamleaderProgressView(Model model){
        var user = userService.getCurrentUser();
        List<Issue> issues=issueRepository.getIssuesByCreator(user.getName());
        List<Issue> currentIssues=new ArrayList<Issue>();
        for(Issue issue:issues){
            if(issue.getProject().getStatus()!=Status.COMPLETED){
                currentIssues.add(issue);

            }
        }
        model.addAttribute("user",user);
        model.addAttribute("currentIssues",currentIssues);
        return "/teamleader-progress-view";
    }


    @GetMapping("/issuedetails/{id}")
    public String ViewIssueDetails(@PathVariable Long id, Model model) {
        IssueDetailsDto issue=issueService.getIssueDetailsById(id);
        model.addAttribute("issue",issue);
//          log.info("Issue -> {}",assignIssue.getIssue().getFilesList());
//        log.info("Issue -> {}",assignIssue.getIssue().getFiles());
        return "issue-details";
    }
}
