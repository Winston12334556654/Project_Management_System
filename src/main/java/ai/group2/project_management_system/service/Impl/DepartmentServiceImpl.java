package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.repository.DepartmentRepository;
import ai.group2.project_management_system.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.getReferenceById(id);
    }


}
