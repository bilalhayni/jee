package com.example.labomasi.service;

import com.example.labomasi.dto.response.DashboardStats;
import com.example.labomasi.enums.ProjectStatus;
import com.example.labomasi.mapper.ProjectMapper;
import com.example.labomasi.mapper.PublicationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final PublicationService publicationService;
    private final ResourceService resourceService;
    private final ProjectMapper projectMapper;
    private final PublicationMapper publicationMapper;

    public DashboardService(ProjectService projectService, MemberService memberService,
                            PublicationService publicationService, ResourceService resourceService,
                            ProjectMapper projectMapper, PublicationMapper publicationMapper) {
        this.projectService = projectService;
        this.memberService = memberService;
        this.publicationService = publicationService;
        this.resourceService = resourceService;
        this.projectMapper = projectMapper;
        this.publicationMapper = publicationMapper;
    }

    public DashboardStats getStats() {
        return DashboardStats.builder()
                .totalProjects(projectService.count())
                .activeProjects(projectService.countByStatus(ProjectStatus.IN_PROGRESS))
                .completedProjects(projectService.countByStatus(ProjectStatus.COMPLETED))
                .plannedProjects(projectService.countByStatus(ProjectStatus.PLANNED))
                .totalMembers(memberService.count())
                .activeMembers(memberService.countActive())
                .totalPublications(publicationService.count())
                .publicationsThisYear(publicationService.countThisYear())
                .totalResources(resourceService.count())
                .availableResources(resourceService.countAvailable())
                .recentProjects(projectMapper.toResponseList(projectService.findRecent()))
                .recentPublications(publicationMapper.toResponseList(publicationService.findRecent()))
                .build();
    }
}
