package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.AssignIssue;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignIssueRepository extends JpaRepository<AssignIssue,Integer> {
}
