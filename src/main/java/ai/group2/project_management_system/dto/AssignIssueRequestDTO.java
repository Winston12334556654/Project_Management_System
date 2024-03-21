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

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public List<User> getMemberUsers() {
        return memberUsers;
    }

    public void setMemberUsers(List<User> memberUsers) {
        this.memberUsers = memberUsers;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueCategory() {
        return issueCategory;
    }

    public void setIssueCategory(String issueCategory) {
        this.issueCategory = issueCategory;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public LocalDate getIssuePlanStartDate() {
        return issuePlanStartDate;
    }

    public void setIssuePlanStartDate(LocalDate issuePlanStartDate) {
        this.issuePlanStartDate = issuePlanStartDate;
    }

    public LocalDate getIssuePlanEndDate() {
        return issuePlanEndDate;
    }

    public void setIssuePlanEndDate(LocalDate issuePlanEndDate) {
        this.issuePlanEndDate = issuePlanEndDate;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }


}
