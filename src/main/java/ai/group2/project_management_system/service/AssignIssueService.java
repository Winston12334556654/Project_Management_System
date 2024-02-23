package ai.group2.project_management_system.service;

import ai.group2.project_management_system.dto.AssignIssueRequestDTO;
import ai.group2.project_management_system.dto.IssueDetailsDto;
import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Issue;

import java.util.List;

public interface AssignIssueService {
    AssignIssueRequestDTO getAssignIssuesByIssueId(Long id);
    AssignIssue save(AssignIssue assignIssue);

    List<AssignIssue> getAssignIssuesByTeamleaderId(Long id);

}
