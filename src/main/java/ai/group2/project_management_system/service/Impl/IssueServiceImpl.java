package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.dto.IssueDetailsDto;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.repository.IssueFileRepository;
import ai.group2.project_management_system.repository.IssueRepository;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.service.IssueService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Issue> getAllIssueByUserId(Long userId) {
        return issueRepository.findAllIssueByUserId(userId);
    }



    @Override
    public List<Issue> findIssuesByProjectId(Long projectId) {
        return issueRepository.findIssuesByProjectId(projectId);
    }

    @Override
    public int getIssueCount() {
        return (int) issueRepository.count();
    }

    @Override
    public int getActiveIssueCount() {
        List<Issue> allIssues = issueRepository.findAll();
        int activeIssues = 0;
        for (Issue issue : allIssues){
            if(issue.isActive()){
                activeIssues++;
            }
        }
        return activeIssues;
    }

    @Override
    public int getInactiveIssueCount() {
        List<Issue> allIssues = issueRepository.findAll();
        int inActiveIssues = 0;
        for (Issue issue : allIssues){
            if(!issue.isActive()){
                inActiveIssues++;
            }
        }
        return inActiveIssues;
    }

    @Override
    public Issue getIssueById(Long id) {
        return issueRepository.getIssueById(id) ;
    }

    @Override
    public Map<String, Integer> getCountsByStatus() {
        Map<String, Integer> countsByStatus = new HashMap<>();
        countsByStatus.put("todo", issueRepository.countByStatus(Status.TODO));
        countsByStatus.put("inProgress", issueRepository.countByStatus(Status.INPROGRESS));
        countsByStatus.put("pending", issueRepository.countByStatus(Status.PENDING));
        /*countsByStatus.put("overdue", issueRepository.countByStatus(Status.OVERDUE));*/
        countsByStatus.put("completed", issueRepository.countByStatus(Status.COMPLETED));
        return countsByStatus;
    }


//
//    @Override
//    public List<Issue> getIssueByUserId(long id) {
//        return issueRepository.getIssueByUserId(id);
//    }
}
