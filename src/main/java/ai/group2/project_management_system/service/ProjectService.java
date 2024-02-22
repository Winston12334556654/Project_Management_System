package ai.group2.project_management_system.service;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectService {
    Project save(Project project);

    Project getProjectBy_Id(Long projectId);

    List<Project> getAllProjects();
    ProjectDTO getProjectById(Long id);


}
