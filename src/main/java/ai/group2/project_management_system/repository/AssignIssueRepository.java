package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.AssignIssue;
import ai.group2.project_management_system.model.entity.Issue;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignIssueRepository extends JpaRepository<AssignIssue,Integer> {
    @Query(value = "SELECT assign_issue.*\n" +
            "FROM assign_issue\n" +
            "JOIN issue ON assign_issue.issue_id = issue.id\n" +
            "JOIN project ON issue.project_id = project.id\n" +
            "WHERE issue.team_leader_id = :teamleaderId AND project.status != 'COMPLETED'",nativeQuery = true)
    List<AssignIssue> getAssignIssuesByTeamLeaderId(@Param("teamleaderId") Long teamleaderId);


    @Query("SELECT ai FROM AssignIssue ai WHERE ai.user.id = :userId")
    List<AssignIssue> getAssignIssuesByMemberId(@Param("userId") Long userId);

    AssignIssue getAssignIssueById(Long id);


}
