package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.model.entity.*;
import ai.group2.project_management_system.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IssueAPI {

    private final ProjectService projectService;
    private final IssueCategoryService issueCategoryService;
    private final UserService userService;
    private final IssueService issueService;
    private final IssueFilesService issueFilesService;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/get-projects")
    public ResponseEntity<List<Project>> getProjects(){
        List<Project> projectList = projectService.getAllProjects();
        return ResponseEntity.ok(projectList);
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
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);

        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/create-issue")
    public ResponseEntity<Issue> createIssue(@RequestParam("issue") String issueJson, @RequestParam("files") List<MultipartFile> files)  throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Issue issue = objectMapper.readValue(issueJson,Issue.class);
        issue.setCreator("Project Manager");
        issue.set_active(true);
        issue.set_assigned(false);

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
}
