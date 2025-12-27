package com.example.labomasi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {

    private long totalProjects;

    private long activeProjects;

    private long completedProjects;

    private long plannedProjects;

    private long totalMembers;

    private long activeMembers;

    private long totalPublications;

    private long publicationsThisYear;

    private long totalResources;

    private long availableResources;

    private List<ProjectResponse> recentProjects;

    private List<PublicationResponse> recentPublications;
}
