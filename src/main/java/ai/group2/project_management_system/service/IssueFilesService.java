package ai.group2.project_management_system.service;

import ai.group2.project_management_system.model.entity.IssueFiles;

import java.util.List;

public interface IssueFilesService {
    IssueFiles save(IssueFiles files);
    List<IssueFiles> findByIssueIds(Long id);
}
