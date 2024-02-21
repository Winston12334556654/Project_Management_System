package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {
    Optional<Issue> findIssueById(Long id);
    List<Issue> getIssuesByTeamLeaderId(Long id);
    @Query(value = "select u.id from issue ie join\n" +
            "project p on ie.project_id = p.id join\n" +
            "project_member pm on p.id = pm.project_id join\n" +
            "user u on pm.user_id = u.id\n" +
            "where ie.id = :issueId and u.role = :role", nativeQuery = true)
    List<Long> getUsersByIssueId(@Param("issueId") Long issueId, @Param("role") String role);



}
