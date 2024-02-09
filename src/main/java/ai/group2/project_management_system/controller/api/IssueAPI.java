package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.IssueCategory;
import ai.group2.project_management_system.service.IssueCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueAPI {

    private final IssueCategoryService issueCategoryService;

    @GetMapping("/get-category")
    public ResponseEntity<List<IssueCategory>> getIssueCategory() {
        List<IssueCategory> issueCategories = issueCategoryService.getAllIssueCategories();
        return ResponseEntity.ok(issueCategories);
    }
}
