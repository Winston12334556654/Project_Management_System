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
    private boolean is_active;
    private boolean is_assigned;

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

}
