package ai.group2.project_management_system.service;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProjectService {
    Project save(Project project);
    List<Project> getAllProjectsWithUsers();
    Project getProjectBy_Id(Long projectId);
    List<Project> getAllProjects();
    ProjectDTO getProjectById(Long id);
    List<Long> getUserIdsByProjectId(Long projectId);
    Project findById(Long projectId);
    int getProjectCount();
    int getActiveProjectCount();
    int getInactiveProjectCount();
    List<Project> getActiveProjects();
    List<Project> getProjectsByDepartmentId(Long departmentId);
    List<Long> getAllProjectIds();
    Map<String, Integer> getCountsByStatus();

    // New method to get project name by ID
    String getProjectNameById(Long projectId);
    String getProjectCreatorByPID(Long projectId);
    List<Project> getProjectsByCreator(String creatorName);
    List<Project> getProjectsByUserId(Long userId);
    Project getProjectByTitle(String title);
    List<Project> getProjectsByUserName(String name);

    List<User> getTeamLeaders(long projectId);
}
