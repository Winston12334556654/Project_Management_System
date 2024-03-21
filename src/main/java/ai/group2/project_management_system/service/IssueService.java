package ai.group2.project_management_system.service;

import ai.group2.project_management_system.dto.IssueDetailsDto;
import ai.group2.project_management_system.model.entity.Issue;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface IssueService {

    Issue save (Issue issue);

    List<Issue> getAllIssues();

    List<Issue> getIssuesByTeamleaderId(Long id);

    IssueDetailsDto getIssueDetailsById(Long id);

    List<Issue> getAllIssueByUserId(Long userId);



    List<Issue> findIssuesByProjectId(Long projectId);

//    List<Issue> getIssueByUserId(Long id);

    int getIssueCount();


    int getActiveIssueCount();

    int getInactiveIssueCount();
    Issue getIssueById(Long id);

    Map<String, Integer> getCountsByStatus();
}
