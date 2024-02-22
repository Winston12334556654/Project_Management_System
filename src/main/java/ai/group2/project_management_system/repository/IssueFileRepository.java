package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.IssueFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueFileRepository extends JpaRepository<IssueFiles, Long> {
    List<IssueFiles> findByIssueId(Long id);
}
