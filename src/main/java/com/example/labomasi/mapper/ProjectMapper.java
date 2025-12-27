package com.example.labomasi.mapper;

import com.example.labomasi.dto.request.ProjectRequest;
import com.example.labomasi.dto.response.ProjectResponse;
import com.example.labomasi.entity.Project;
import com.example.labomasi.enums.ProjectStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    private final MemberMapper memberMapper;

    public ProjectMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public ProjectResponse toResponse(Project project) {
        if (project == null) {
            return null;
        }

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .statusDisplayName(project.getStatus() != null ? project.getStatus().getDisplayName() : null)
                .budget(project.getBudget())
                .fundingSource(project.getFundingSource())
                .lead(memberMapper.toResponse(project.getLead()))
                .members(project.getMembers() != null ?
                        project.getMembers().stream()
                                .map(memberMapper::toResponse)
                                .collect(Collectors.toSet()) : new HashSet<>())
                .progress(project.getProgress())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public List<ProjectResponse> toResponseList(List<Project> projects) {
        if (projects == null) {
            return null;
        }
        return projects.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Project toEntity(ProjectRequest request) {
        if (request == null) {
            return null;
        }

        return Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : ProjectStatus.PLANNED)
                .budget(request.getBudget())
                .fundingSource(request.getFundingSource())
                .members(new HashSet<>())
                .build();
    }

    public void updateEntity(Project project, ProjectRequest request) {
        if (project == null || request == null) {
            return;
        }

        if (request.getTitle() != null) {
            project.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            project.setEndDate(request.getEndDate());
        }
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        if (request.getBudget() != null) {
            project.setBudget(request.getBudget());
        }
        if (request.getFundingSource() != null) {
            project.setFundingSource(request.getFundingSource());
        }
    }
}
