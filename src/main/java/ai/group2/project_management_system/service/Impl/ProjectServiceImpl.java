package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.repository.*;
import ai.group2.project_management_system.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }


}
