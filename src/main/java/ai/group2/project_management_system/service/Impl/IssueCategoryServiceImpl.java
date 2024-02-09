package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.IssueCategory;
import ai.group2.project_management_system.repository.IssueCategoryRepository;
import ai.group2.project_management_system.service.IssueCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueCategoryServiceImpl implements IssueCategoryService {

    private final IssueCategoryRepository issueCategoryRepository;

    @Override
    public IssueCategory save(IssueCategory issueCategory) {
        return issueCategoryRepository.save(issueCategory);
    }

    @Override
    public List<IssueCategory> getAllIssueCategories() {
        return issueCategoryRepository.findAll();
    }
}
