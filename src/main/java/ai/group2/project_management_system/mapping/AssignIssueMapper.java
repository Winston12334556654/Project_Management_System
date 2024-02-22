package ai.group2.project_management_system.mapping;

import ai.group2.project_management_system.dto.AssignIssueDTO;
import ai.group2.project_management_system.model.Enum.Priority;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.AssignIssue;
import org.springframework.stereotype.Service;

@Service
public class AssignIssueMapper {
    public static AssignIssue mapAssignIssueToAssignIssue1(AssignIssueDTO assignIssueDTO) {
        AssignIssue assignIssue = new AssignIssue();
        assignIssue.setName(assignIssueDTO.getAssignName());
        assignIssue.set_active(true);
        assignIssue.setUser(assignIssueDTO.getUser());
        assignIssue.setStatus(Status.valueOf(assignIssueDTO.getStatus()));
        assignIssue.setPriority(Priority.valueOf(assignIssueDTO.getPriority()));
        assignIssue.setPlanStartDate(assignIssueDTO.getPlanStartDate());
        assignIssue.setPlanDueDate(assignIssueDTO.getPlanDueDate());
        assignIssue.setIssue(assignIssueDTO.getIssue());
        return assignIssue;
    }
}
