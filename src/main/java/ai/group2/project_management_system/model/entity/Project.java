package ai.group2.project_management_system.model.entity;

import ai.group2.project_management_system.model.Enum.Category;
import ai.group2.project_management_system.model.Enum.Priority;
import ai.group2.project_management_system.model.Enum.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String objective;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String creator;

    private LocalDate planStartDate;

    private LocalDate planEndDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Department department;
}
