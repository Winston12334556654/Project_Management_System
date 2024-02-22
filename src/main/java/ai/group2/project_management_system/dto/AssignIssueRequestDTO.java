package ai.group2.project_management_system.dto;

import ai.group2.project_management_system.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignIssueRequestDTO {
    private Long issueId;
    private List<User> memberUsers;
    private String projectName;
    private String issueTitle;
    private String issueType;
    private String issueCategory;
    private String subject;
    private String issueDescription;
    private LocalDate issuePlanStartDate;
    private LocalDate issuePlanEndDate;
    private List<String> fileNameList;

}
