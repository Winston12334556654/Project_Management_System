package ai.group2.project_management_system.dto;

import ai.group2.project_management_system.model.Enum.Category;
import ai.group2.project_management_system.model.Enum.Priority;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String title;
    private String objective;
    private String description;
    private String creator;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
    private Status status;
    private Category category;
    private Priority priority;

    private boolean is_active;

    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private List<Long> userIds;

    public ProjectDTO(Project project) {
        id = project.getId();
        title = project.getTitle();
        objective = project.getObjective();
        description = project.getDescription();
        creator = project.getCreator();
        planStartDate = project.getPlanStartDate();
        planEndDate = project.getPlanEndDate();
        status = project.getStatus();
        category = project.getCategory();
        priority = project.getPriority();
        is_active = project.is_active();
        actualStartDate = project.getActualStartDate();
        actualEndDate = project.getActualEndDate();
        userIds = project.getUserIds().stream().toList();

    }
}
