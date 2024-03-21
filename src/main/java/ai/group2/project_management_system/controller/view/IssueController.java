package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.dto.IssueDetailsDto;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.service.AssignIssueService;
import ai.group2.project_management_system.service.IssueService;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.DecimalFormat;
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

    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session) {
        User user = userService.getCurrentUser();
        return user;
    }

    @GetMapping("/issue-list")
    public String issueList(Model model){
        var user = userService.getCurrentUser();
        List<Issue> pmIssues=issueRepository.getIssuesByCreator(user.getName());
        for (Issue issue : pmIssues) {

                if (issue.getStatus() != Status.COMPLETED && issue.getStatus() != Status.PENDING) {
                    if( issue.getPlanDueDate().isBefore(LocalDate.now())){
                        /*issue.setStatus(Status.OVERDUE);*/
                        issueRepository.save(issue);
                    }

                }

        }
    //    log.info("Issue -> {}",pmIssues.size());
        model.addAttribute("pmIssues",pmIssues);
        return "issue-list";
    }
    @GetMapping("/issueboard")
    public String issueBoard(Model model){
        int todoCount = 0;
         int  inprogressCount=0;
          int  pendingCount=0;
            int completedCount=0;
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

        /*For Overdue*/
        for(Project project:currentProjects){

            if (project.getStatus() != Status.COMPLETED && project.getStatus() != Status.PENDING) {
                if( project.getPlanEndDate().isBefore(LocalDate.now())){
                    /*project.setStatus(Status.OVERDUE);*/
                    projectRepository.save(project);
                }
            }
        }

       /* For percentage*/
        double percentage=0;
        for(Project pj:currentProjects){
            int issueCount = issueRepository.countIssuesByProjectId(pj.getId());
            int completedIssueCount = issueRepository.countCompletedIssuesByProjectId(pj.getId());
            percentage = (double) completedIssueCount / issueCount * 100;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedPercentage = decimalFormat.format(percentage);
            pj.setPercentage(formattedPercentage);
            projectRepository.save(pj);

            if(pj.getStatus()==Status.TODO){
                todoCount+=1;
            } else if (pj.getStatus()==Status.INPROGRESS) {
                inprogressCount+=1;
            } else if (pj.getStatus()==Status.PENDING) {
                pendingCount+=1;
            }else {
                completedCount+=1;
            }
        }
//        log.info(" All Project list -> {}",creatorIssues.size());
//          log.info("Project list -> {}",currentIssues.size());
        model.addAttribute("todoCount", todoCount);
        model.addAttribute("inprogressCount", inprogressCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("completedCount", completedCount);
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
        System.out.println("Issue Id:"+id);
        return "issue-details";
    }
}
