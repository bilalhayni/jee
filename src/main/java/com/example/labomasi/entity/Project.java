package com.example.labomasi.entity;

import com.example.labomasi.enums.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.PLANNED;

    private String budget;

    @Column(name = "funding_source")
    private String fundingSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Member lead;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @Builder.Default
    private Set<Member> members = new HashSet<>();

    public int getProgress() {
        if (status == ProjectStatus.COMPLETED) return 100;
        if (status == ProjectStatus.PLANNED || status == ProjectStatus.ON_HOLD) return 0;
        if (startDate == null || endDate == null) return 0;

        LocalDate now = LocalDate.now();
        if (now.isBefore(startDate)) return 0;
        if (now.isAfter(endDate)) return 100;

        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        long elapsedDays = ChronoUnit.DAYS.between(startDate, now);

        return totalDays > 0 ? (int) ((elapsedDays * 100) / totalDays) : 0;
    }
}