package ai.group2.project_management_system.service;


import ai.group2.project_management_system.model.entity.IssueCategory;

import java.util.List;

public interface IssueCategoryService {
    IssueCategory save(IssueCategory issueCategory);

    List<IssueCategory> getAllIssueCategories();
}
