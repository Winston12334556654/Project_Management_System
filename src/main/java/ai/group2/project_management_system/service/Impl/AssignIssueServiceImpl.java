package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.dto.AssignIssueRequestDTO;
import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.AssignIssueRepository;
import ai.group2.project_management_system.repository.IssueFileRepository;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.repository.UserRepository;
import ai.group2.project_management_system.service.AssignIssueService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class AssignIssueServiceImpl implements AssignIssueService {
    private final IssueRepository issueRepository;
    private final IssueFileRepository issueFileRepository;
    private final UserRepository userRepository;
    private final AssignIssueRepository assignIssueRepository;
    @Override
    public AssignIssueRequestDTO getAssignIssuesByIssueId(Long id) {
        Issue issue =  issueRepository.findIssueById(id).orElse(null);
        var issueFiles = issueFileRepository.findByIssueId(id);
        List<Long> pjMemberIds = issueRepository.getUsersByIssueId(id, "MEMBER");
        List<User> members = userRepository.findAllById(pjMemberIds);

        return AssignIssueRequestDTO.builder()
                .issueId(issue.getId())
                .memberUsers(members)
                .projectName(issue.getProject().getTitle())
                .issueTitle(issue.getTitle())
                .issueType(issue.getIssueType())
                .issueCategory(issue.getIssueCategory().getName())
                .subject(issue.getSubject())
                .issueDescription(issue.getDescription())
                .issuePlanStartDate(issue.getPlanStartDate())
                .issuePlanEndDate(issue.getPlanDueDate())
                .fileNameList(issueFiles.stream().map(v->v.getFileName()).toList())
                .build();
    }

    @Override
    public AssignIssue save(AssignIssue assignIssue) {
        return assignIssueRepository.save(assignIssue);
    }
}
