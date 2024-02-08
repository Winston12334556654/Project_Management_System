package ai.group2.project_management_system.model.entity;

import jakarta.persistence.*;


import java.util.Date;

@Entity
@Table(name = "assign_issue")
public class AssignIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column(name = "status")
    private String status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "plan_start_date")
    private Date planStartDate;

    @Column(name = "plan_due_date")
    private Date planDueDate;

    @Column(name = "actual_start_date")
    private Date actualStartDate;

    @Column(name = "actual_due_date")
    private Date actualDueDate;
}
