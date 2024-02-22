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
        Department newDepartment = departmentService.save(department);
        return ResponseEntity.ok(newDepartment);
    }

    @PutMapping("/edit-department/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable("id") Long departmentId, @RequestBody Department updatedDepartment) {
        Department existingDepartment = departmentService.getDepartmentById(departmentId);

        if (existingDepartment != null) {
            // Update the existing department with the new information
            existingDepartment.setName(updatedDepartment.getName());
            existingDepartment.setEmail(updatedDepartment.getEmail());
            existingDepartment.setPhone(updatedDepartment.getPhone());
            existingDepartment.set_active(updatedDepartment.is_active());
            // Update other fields as needed

            Department savedDepartment = departmentService.save(existingDepartment);
            return ResponseEntity.ok(savedDepartment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/department-toggle/{departmentId}")
    public ResponseEntity<String> toggleDepartmentStatus(@PathVariable("departmentId") Long departmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        if (department != null) {
            department.set_active(!department.is_active());
            departmentService.save(department);
            return ResponseEntity.ok("Department status changed successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
