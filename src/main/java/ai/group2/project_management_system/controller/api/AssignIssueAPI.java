package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.dto.AssignIssueRequestDTO;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.repository.AssignIssueRepository;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.service.AssignIssueService;
import ai.group2.project_management_system.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AssignIssueAPI {
    private final AssignIssueService assignIssueService;

    private final IssueRepository issueRepository;
    private final AssignIssueRepository assignIssueRepository;

    @GetMapping("/teamleaderissues/{issueId}")
    public ResponseEntity<AssignIssueRequestDTO> getDepartments(@PathVariable("issueId") Long issueId) {
        AssignIssueRequestDTO assignIssueRequestDTO=assignIssueService.getAssignIssuesByIssueId(issueId);
        System.out.println("Assign Issue Request DTO: "+assignIssueRequestDTO);
        return ResponseEntity.ok(assignIssueRequestDTO);
    }

    @PutMapping("/assign-issue/{assignIssueId}")
    public ResponseEntity<String> updateAssignIssueStatus(@PathVariable("assignIssueId") Long assignIssueId,
                                                          @RequestBody AssignIssue requestAssignIssue                                              ) {
         //   String newStatus= String.valueOf(requestAssignIssue.getStatus());
        AssignIssue assignIssue = assignIssueRepository.findById(Math.toIntExact(assignIssueId)).orElse(null);

        if (assignIssue != null) {
            assignIssue.setStatus(requestAssignIssue.getStatus());
            if(requestAssignIssue.getStatus() .equals(Status.INPROGRESS)){
                assignIssue.setActualStartDate(LocalDate.now());
            }

            if(requestAssignIssue.getStatus() .equals(Status.COMPLETED)){
                assignIssue.setActualDueDate(LocalDate.now());
            }
            assignIssueRepository.save(assignIssue);

            return ResponseEntity.ok(String.format("AssignIssue %d status updated to %s", assignIssueId, requestAssignIssue.getStatus()));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/get-all-subIssues/{memberId}")
    public ResponseEntity<List<AssignIssue>> getAllAssignIssue(@PathVariable("memberId") Long memberId) {
        List<AssignIssue> assignIssues=assignIssueService.getAssignIssuesByMemberId(memberId);
        return ResponseEntity.ok(assignIssues);
    }

}
