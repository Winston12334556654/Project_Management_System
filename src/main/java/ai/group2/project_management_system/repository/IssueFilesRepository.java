package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.IssueFiles;
import ai.group2.project_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueFilesRepository extends JpaRepository<IssueFiles,Long> {
    List<IssueFiles> findAllByIssueId(Long id);
}
