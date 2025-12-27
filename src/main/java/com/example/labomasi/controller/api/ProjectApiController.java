package com.example.labomasi.controller.api;

import com.example.labomasi.dto.request.ProjectRequest;
import com.example.labomasi.dto.response.ProjectResponse;
import com.example.labomasi.entity.Member;
import com.example.labomasi.entity.Project;
import com.example.labomasi.exception.ResourceNotFoundException;
import com.example.labomasi.mapper.ProjectMapper;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/projects")
public class ProjectApiController {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final ProjectMapper projectMapper;

    public ProjectApiController(ProjectService projectService, MemberService memberService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.memberService = memberService;
        this.projectMapper = projectMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        return ResponseEntity.ok(projectMapper.toResponseList(projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        return ResponseEntity.ok(projectMapper.toResponse(project));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> searchProjects(@RequestParam String query) {
        List<Project> projects = projectService.search(query);
        return ResponseEntity.ok(projectMapper.toResponseList(projects));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ProjectResponse>> getRecentProjects() {
        List<Project> projects = projectService.findRecent();
        return ResponseEntity.ok(projectMapper.toResponseList(projects));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request) {
        Project project = projectMapper.toEntity(request);

        if (request.getLeadId() != null) {
            Member lead = memberService.findById(request.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member", request.getLeadId()));
            project.setLead(lead);
        }

        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            Set<Member> members = new HashSet<>();
            for (Long memberId : request.getMemberIds()) {
                Member member = memberService.findById(memberId)
                        .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
                members.add(member);
            }
            project.setMembers(members);
        }

        Project savedProject = projectService.save(project);
        return new ResponseEntity<>(projectMapper.toResponse(savedProject), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {

        Project project = projectService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));

        projectMapper.updateEntity(project, request);

        if (request.getLeadId() != null) {
            Member lead = memberService.findById(request.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member", request.getLeadId()));
            project.setLead(lead);
        }

        if (request.getMemberIds() != null) {
            Set<Member> members = new HashSet<>();
            for (Long memberId : request.getMemberIds()) {
                Member member = memberService.findById(memberId)
                        .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
                members.add(member);
            }
            project.setMembers(members);
        }

        Project updatedProject = projectService.save(project);
        return ResponseEntity.ok(projectMapper.toResponse(updatedProject));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Project", id);
        }
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
