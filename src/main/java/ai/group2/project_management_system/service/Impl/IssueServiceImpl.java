package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.dto.IssueDetailsDto;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.repository.IssueFileRepository;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.service.IssueService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final IssueFileRepository issueFileRepository;

    @Override
    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    @Override
    public List<Issue> getIssuesByTeamleaderId(Long id) {
        return issueRepository.getIssuesByTeamLeaderId(id);
    }

    @Override
    public IssueDetailsDto getIssueDetailsById(Long id){

        Issue issue =  issueRepository.findIssueById(id).orElse(null);
        var issueFiles = issueFileRepository.findByIssueId(id);

        return IssueDetailsDto.builder()
                .issueCategory(issue.getIssueCategory().getName())
                .issueDescription(issue.getDescription())
                .issueType(issue.getIssueType())
                .issueTitle(issue.getTitle())
                .actualEndDate(issue.getActualDueDate())
                .actualStartDate(issue.getActualStartDate())
                .dueDate(issue.getPlanDueDate())
                .startDate(issue.getPlanStartDate())
                .pmName(issue.getCreator())
                .fileNameList(issueFiles.stream().map(v-> v.getFileName()).toList())
                .priority(issue.getPriority().name())
                .status(issue.getStatus().name())
                .subject(issue.getSubject())
                .projectName(issue.getProject().getTitle())
                .build();
    }
}
