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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(LocalDate planStartDate) {
        this.planStartDate = planStartDate;
    }

    public LocalDate getPlanDueDate() {
        return planDueDate;
    }

    public void setPlanDueDate(LocalDate planDueDate) {
        this.planDueDate = planDueDate;
    }

    public LocalDate getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public LocalDate getActualDueDate() {
        return actualDueDate;
    }

    public void setActualDueDate(LocalDate actualDueDate) {
        this.actualDueDate = actualDueDate;
    }

}
