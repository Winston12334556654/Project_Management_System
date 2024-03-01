package ai.group2.project_management_system.dto;

import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignIssueDTO {
    private Long id;
    private Issue issue;
    private String assignName;
    private boolean isActive;
    private User user;
    private String status;
    private String priority;
    private LocalDate planStartDate;
    private LocalDate planDueDate;
    private LocalDate actualStartDate;
    private LocalDate actualDueDate;

}
