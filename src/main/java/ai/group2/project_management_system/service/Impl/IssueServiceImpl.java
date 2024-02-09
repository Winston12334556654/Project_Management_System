package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    @Override
    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }
}
