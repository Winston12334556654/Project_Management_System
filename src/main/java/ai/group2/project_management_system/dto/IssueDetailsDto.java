package ai.group2.project_management_system.dto;

import ai.group2.project_management_system.model.Enum.Priority;
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
public class IssueDetailsDto {
    private String pmName;
    private String projectName;
    private String issueTitle;
    private String priority;
    private String status;
    private String issueType;
    private String issueCategory;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private List<String> fileNameList;
    private String subject;
    private String issueDescription;

}
