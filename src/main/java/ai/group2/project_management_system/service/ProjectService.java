package ai.group2.project_management_system.service;

import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.repository.UserRepository;

import java.util.List;

public interface ProjectService {
    Project save(Project project);

    List<Project> getAllProject();
}
