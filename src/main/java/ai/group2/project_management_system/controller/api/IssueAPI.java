package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.*;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.repository.UserRepository;
import ai.group2.project_management_system.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class IssueAPI {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final IssueCategoryService issueCategoryService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final IssueService issueService;
    private final IssueFilesService issueFilesService;
    private final IssueRepository issueRepository;

    @Autowired
    private EmailService service;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/get-projects/{user}")
    public ResponseEntity<List<Project>> getProjects(@PathVariable String user){
        List<Project> projectList =projectRepository.findProjectsByCreator(user);
        /*List<Project> projectList = projectService.getAllProjects();*/
        List<Project> currentProjectList=new ArrayList<Project>();
        for(Project project:projectList){
            if(project.getStatus()!=Status.COMPLETED && project.getStatus()!=Status.PENDING){
                currentProjectList.add(project);
            }
        }
        return ResponseEntity.ok(currentProjectList);
    }

    @GetMapping("/get-categories")
    public ResponseEntity<List<IssueCategory>> getIssueCategory() {
        List<IssueCategory> issueCategories = issueCategoryService.getAllIssueCategories();
        return ResponseEntity.ok(issueCategories);
    }

    @PostMapping("/add-category")
    public ResponseEntity<IssueCategory> addIssueCategory(@RequestBody IssueCategory issueCategory){
        IssueCategory newIssueCategory = issueCategoryService.save(issueCategory);
        return ResponseEntity.ok(newIssueCategory);
    }

    @GetMapping("/issue-members-selector/{projectId}")
    public ResponseEntity<List<UserDTO>> getUsers(@PathVariable Long projectId) {
        List<UserDTO> users = userService.getUsersByProjectId(projectId);

        if (!users.isEmpty()) {
            users.forEach(System.out::println);
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/get-project/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable Long projectId) {
        Project project = projectService.getProjectBy_Id(projectId);

        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/create-issue")
    public ResponseEntity<Issue> createIssue(@RequestParam("issue") String issueJson, @RequestParam("files") List<MultipartFile> files)  throws JsonProcessingException {
        User user=userService.getCurrentUser();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Issue issue = objectMapper.readValue(issueJson,Issue.class);
        issue.setCreator(user.getName());
        issue.setStatus(Status.TODO);
        issue.setActive(true);
        issue.setAssigned(false);
        issue.setStatus(Status.TODO);


        // Extracting specific fields
        Long projectId = issue.getProject().getId();
        String title = issue.getTitle();
        String projectName= projectService.getProjectNameById(projectId);
        String projectManagerName= projectService.getProjectCreatorByPID(projectId);
        String userEmailById= userService.getUserEmailById(issue.getTeamLeaderId());
        String userNameById= userService.getUserNameById(issue.getTeamLeaderId());


        System.out.println("projecId "+ projectId);
        System.out.println("Title "+ title);
        System.out.println("projectName "+ projectName);
        System.out.println("projectManagerName "+ projectManagerName);
        System.out.println("userEmailById "+ userEmailById);
        System.out.println("userNameById "+ userNameById);

        EmailDetail email= new EmailDetail();
        email.setRecipients(Collections.singletonList(userEmailById));
        email.setSubject("Issue Assign Announcements!");
        email.setMsgBody("Dear Mr-"+userNameById+" You have been assigned in "+title + " Issue of "+projectName+ " Project By the Project Manager Mr-"+projectManagerName+". FOR Further affairs, Please check in the website!" + "http://localhost:8080/home");
        email.setAttachments(files);

        System.out.println("Emails :  "+ email.getRecipients());
        System.out.println("Subject :  "+ email.getSubject());
        System.out.println("message :  "+ email.getMsgBody());
        service.sendMultipleEmail(email);


        List<String> fileNames = saveAttachments(files);
        Issue newIssue = issueService.save(issue);
        saveFileNames(newIssue, fileNames);
        return ResponseEntity.ok(newIssue);
    }

    private List<String> saveAttachments(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            fileNames.add(fileName);
            try {
                file.transferTo(new File(uploadPath + File.separator + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileNames;
    }

    private void saveFileNames(Issue issue, List<String> fileNames) {
       for (String fileName : fileNames) {
            IssueFiles issueFiles = new IssueFiles();
            issueFiles.setIssue(issue);
            issueFiles.setFileName(fileName);
            issueFilesService.save(issueFiles);
        }
    }


    @PutMapping("/team-leader-issue/{issueId}")
    public ResponseEntity<String> updateIssueStatus(@PathVariable("issueId") Long issueId,
                                                          @RequestBody Issue requestIssue                                              ) {
        //   String newStatus= String.valueOf(requestAssignIssue.getStatus());
        Issue issue = issueRepository.findById(Math.toIntExact(issueId)).orElse(null);

        if (issue != null) {
            issue.setStatus(requestIssue.getStatus());
            issue.setActualDueDate(LocalDate.now());
            issueRepository.save(issue);
            return ResponseEntity.ok(String.format("Issue %d status updated to %s", issueId, requestIssue.getStatus()));
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    @GetMapping("/issueList")
    public ResponseEntity<List<Issue>> getIssueList(){

        List<Issue> issues= issueService.getAllIssues();

        // Print the list of issues to the console
        System.out.println("List of Issues: " + issues);
         return ResponseEntity.ok(issues);
    }

    @GetMapping("/get-user/{teamLeaderId}")
    public ResponseEntity<User> getUser(@PathVariable String teamLeaderId) {
        Long id = Long.valueOf(teamLeaderId);
        User user=userService.getUserById(id
        );
        log.info("User -> {}",user);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @GetMapping("/{projectId}/issues")
    public ResponseEntity<List<Issue>> getIssuesByProjectId(@PathVariable Long projectId) {
        List<Issue> issues = issueService.findIssuesByProjectId(projectId);

        if (issues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(issues, HttpStatus.OK);
        }


    }

    @GetMapping("/issueList/{userId}")
    public ResponseEntity<List<Issue>> getIssuesByUserId(@PathVariable Long userId) {
        List<Issue> issues = issueService.getIssuesByTeamleaderId(userId);

        if (issues != null && !issues.isEmpty()) {
            return ResponseEntity.ok(issues);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/updateIssue/{issueId}")
    public ResponseEntity<String> updateIssueActive(@PathVariable("issueId") Long issueId) {
        System.out.println("IssueId"+issueId);
        Issue issue = issueService.getIssueById(issueId);
        if (issue != null && issue.isActive()) {
            issue.setActive(false);
            issueService.save(issue);
            return ResponseEntity.ok("Issue status false changed successfully");
        } else {
            issue.setActive(true);
            issueService.save(issue);
            return ResponseEntity.ok("Issue status true changed successfully");
        }
    }

}
