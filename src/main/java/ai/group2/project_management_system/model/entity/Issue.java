package ai.group2.project_management_system.model.entity;

import ai.group2.project_management_system.model.Enum.Priority;
import ai.group2.project_management_system.model.Enum.Status;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter

public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    private String issueType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(nullable = false)
    private String subject;

    @Column(length = 500)
    private String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    private IssueCategory issueCategory;

    @Column(nullable = false)
    private String creator;
    private boolean isActive;
    private boolean isAssigned;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDate planStartDate;

    @Column(nullable = false)
    private LocalDate planDueDate;

    @Column(nullable = true)
    private LocalDate actualStartDate;

    @Column(nullable = true)
    private LocalDate actualDueDate;

    @OneToMany(mappedBy = "issue")
//    @JsonIgnore
    private List<IssueFiles> filesList;

    @Column(nullable = true)
    private Long teamLeaderId;

    @Transient
    private List<MultipartFile> files;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Project project;

    @OneToMany(mappedBy = "issue")
    private Set<AssignIssue> assignIssues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueCategory getIssueCategory() {
        return issueCategory;
    }

    public void setIssueCategory(IssueCategory issueCategory) {
        this.issueCategory = issueCategory;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public List<IssueFiles> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<IssueFiles> filesList) {
        this.filesList = filesList;
    }

    public Long getTeamLeaderId() {
        return teamLeaderId;
    }

    public void setTeamLeaderId(Long teamLeaderId) {
        this.teamLeaderId = teamLeaderId;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<AssignIssue> getAssignIssues() {
        return assignIssues;
    }

    public void setAssignIssues(Set<AssignIssue> assignIssues) {
        this.assignIssues = assignIssues;
    }
}
