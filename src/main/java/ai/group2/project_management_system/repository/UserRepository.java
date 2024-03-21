package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIdIn(List<Long> userIds);
    List<User> findByDepartmentId(Long departmentId);
    Optional<User> findByEmail(String email);
    List<User> findByProjects_id(Long projectId);
    @Query("SELECT u.profilePictureFileName FROM User u WHERE u.id = :id")
    String findPhotoById(@Param("id") Long id);
    @Query(value = "select u.* from user u join project_member pm on u.id = pm.user_id join issue i on i.project_id=pm.project_id where i.id =:issueId", nativeQuery = true)
    List<User> findAllUserByIssueId(@Param("issueId") Long issueId);

    @Query("SELECT u.department.id FROM User u WHERE u.name =: user")
    Long getDepartmentIdByUser(@Param("userName") String user);

    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId")
    List<Project> findProjectsByUserId(Long userId);

    @Query("SELECT u.email FROM User u WHERE u.id = :userId")
    String findEmailById(@Param("userId") Long userId);

    @Query("SELECT u.name FROM User u WHERE u.id = :userId")
    String findUserNameByID(@Param("userId")Long userId);
    @Query("SELECT u.email FROM User u")
    List<String> findAllEmails();
}
