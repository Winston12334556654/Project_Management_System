package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.IssueCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueCategoryRepository extends JpaRepository<IssueCategory, Integer> {

}
