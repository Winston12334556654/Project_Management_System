package ai.group2.project_management_system.service;

import ai.group2.project_management_system.model.entity.Issue;

import java.util.List;

public interface IssueService {

    Issue save (Issue issue);

    List<Issue> getAllIssues();
}
