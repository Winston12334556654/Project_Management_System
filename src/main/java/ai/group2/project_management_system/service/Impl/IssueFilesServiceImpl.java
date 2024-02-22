package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.IssueFiles;
import ai.group2.project_management_system.repository.IssueFilesRepository;
import ai.group2.project_management_system.service.IssueFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueFilesServiceImpl implements IssueFilesService{

    private final IssueFilesRepository issueFilesRepository;
    @Override
    public IssueFiles save(IssueFiles files) {
        return issueFilesRepository.save(files);
    }

    @Override
    public List<IssueFiles> findByIssueIds(Long id) {
        return issueFilesRepository.findAllByIssueId(id);
    }

}
