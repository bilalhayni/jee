package com.example.labomasi.dto.response;

import com.example.labomasi.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus status;

    private String statusDisplayName;

    private String budget;

    private String fundingSource;

    private MemberResponse lead;

    private Set<MemberResponse> members;

    private int progress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
