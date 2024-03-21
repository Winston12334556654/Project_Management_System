package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DepartmentAPI {

    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @PostMapping("/add-department")
    public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        department.setActive(true);
        Department newDepartment = departmentService.save(department);
        return ResponseEntity.ok(newDepartment);
    }

    @PutMapping("/edit-department/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable("id") Long departmentId, @RequestBody Department updatedDepartment) {
        Department existingDepartment = departmentService.getDepartmentById(departmentId);

        if (existingDepartment != null) {
            existingDepartment.setName(updatedDepartment.getName());
            existingDepartment.setEmail(updatedDepartment.getEmail());
            existingDepartment.setPhone(updatedDepartment.getPhone());

            Department savedDepartment = departmentService.save(existingDepartment);
            return ResponseEntity.ok(savedDepartment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("api/updateRecord/{departmentId}")
    public ResponseEntity<String> departmentStatus(@PathVariable("departmentId") Long departmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        if (department != null && department.isActive()) {
            department.setActive(false);
            departmentService.save(department);
            return ResponseEntity.ok("Department status false changed successfully");
        } else {
            department.setActive(true);
            departmentService.save(department);
            return ResponseEntity.ok("Department status true changed successfully");
        }
    }

}
