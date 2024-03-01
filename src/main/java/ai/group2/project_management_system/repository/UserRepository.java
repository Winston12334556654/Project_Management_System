package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.dto.UserDTO;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIdIn(List<Long> userIds);
    List<User> findByDepartmentId(Long departmentId);
    Optional<User> findByEmail(String email);
    List<User> findByProjects_id(Long projectId);
}
