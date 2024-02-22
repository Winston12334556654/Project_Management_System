package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.dto.AssignIssueRequestDTO;
import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.service.AssignIssueService;
import ai.group2.project_management_system.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssignIssueAPI {
    private final AssignIssueService assignIssueService;

    private final IssueRepository issueRepository;

    @GetMapping("/teamleaderissues/{issueId}")
    public ResponseEntity<AssignIssueRequestDTO> getDepartments(@PathVariable("issueId") Long issueId) {
        AssignIssueRequestDTO assignIssueRequestDTO=assignIssueService.getAssignIssuesByIssueId(issueId);
        System.out.println("Assign Issue Request DTO: "+assignIssueRequestDTO);
        return ResponseEntity.ok(assignIssueRequestDTO);
    }
}
