package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

     @Query("SELECT p FROM Project p WHERE p.id = :projectId")
     Project findProjectById(@Param("projectId") Long projectId);
     List<Project> findProjectsByCreator(String creator);

     @Query("SELECT p.id FROM Project p")
     List<Long> findAllProjectIds();
     @Query("SELECT DISTINCT p FROM Project p " +
             "JOIN p.users u " +
             "WHERE u.id = :userId")
     List<Project> findProjectsByUserId(@Param("userId") Long userId);
    Integer countByStatus(Status status);


     // New method to find project name by ID
     @Query("SELECT p.title FROM Project p WHERE p.id = :projectId")
     String findProjectNameById(@Param("projectId") Long projectId);

    // New method to find project creator by ID
    @Query("SELECT p.creator FROM Project p WHERE p.id = :projectId")
    String findProjectCreatorById(@Param("projectId") Long projectId);


    @Query("SELECT p FROM Project p WHERE p.title = :title")
    Project findProjectByTitle(@Param("title") String title);

    @Query("SELECT p FROM Project p WHERE p.creator = :creator")
    List<Project> findProjectsByUserName(@Param("creator") String name);

    @Query("SELECT u FROM User u JOIN Issue i ON u.id = i.teamLeaderId WHERE i.project.id= :projectId")
    List<User> findTeamLeadersByProjectId(@Param("projectId") Long projectId);

}
