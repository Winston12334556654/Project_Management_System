package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class ReportAPI {
    private final IssueService issueService;
    @GetMapping("/get-issues-by-teamLeaderId/{teamLeaderId}")
    public ResponseEntity<List<Issue>> getIssues(@PathVariable Long teamLeaderId) {
        List<Issue> issues = issueService.getIssuesByTeamleaderId(teamLeaderId);
        return ResponseEntity.ok(issues);
    }
}

