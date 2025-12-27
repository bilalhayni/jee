package com.example.labomasi.controller.api;

import com.example.labomasi.dto.response.DashboardStats;
import com.example.labomasi.enums.ProjectStatus;
import com.example.labomasi.mapper.ProjectMapper;
import com.example.labomasi.mapper.PublicationMapper;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import com.example.labomasi.service.PublicationService;
import com.example.labomasi.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final PublicationService publicationService;
    private final ResourceService resourceService;
    private final ProjectMapper projectMapper;
    private final PublicationMapper publicationMapper;

    public DashboardApiController(ProjectService projectService, MemberService memberService,
                                  PublicationService publicationService, ResourceService resourceService,
                                  ProjectMapper projectMapper, PublicationMapper publicationMapper) {
        this.projectService = projectService;
        this.memberService = memberService;
        this.publicationService = publicationService;
        this.resourceService = resourceService;
        this.projectMapper = projectMapper;
        this.publicationMapper = publicationMapper;
    }

    @GetMapping
    public ResponseEntity<DashboardStats> getDashboardStats() {
        DashboardStats stats = DashboardStats.builder()
                .totalProjects(projectService.count())
                .activeProjects(projectService.countByStatus(ProjectStatus.IN_PROGRESS))
                .completedProjects(projectService.countByStatus(ProjectStatus.COMPLETED))
                .plannedProjects(projectService.countByStatus(ProjectStatus.PLANNED))
                .totalMembers(memberService.count())
                .activeMembers(memberService.count())
                .totalPublications(publicationService.count())
                .publicationsThisYear(publicationService.countThisYear())
                .totalResources(resourceService.count())
                .availableResources(resourceService.countAvailable())
                .recentProjects(projectMapper.toResponseList(projectService.findRecent()))
                .recentPublications(publicationMapper.toResponseList(publicationService.findRecent()))
                .build();

        return ResponseEntity.ok(stats);
    }
}
