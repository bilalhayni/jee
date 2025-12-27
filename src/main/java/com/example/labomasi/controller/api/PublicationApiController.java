package com.example.labomasi.controller.api;

import com.example.labomasi.dto.request.PublicationRequest;
import com.example.labomasi.dto.response.PublicationResponse;
import com.example.labomasi.entity.Member;
import com.example.labomasi.entity.Project;
import com.example.labomasi.entity.Publication;
import com.example.labomasi.exception.ResourceNotFoundException;
import com.example.labomasi.mapper.PublicationMapper;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import com.example.labomasi.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
public class PublicationApiController {

    private final PublicationService publicationService;
    private final MemberService memberService;
    private final ProjectService projectService;
    private final PublicationMapper publicationMapper;

    public PublicationApiController(PublicationService publicationService, MemberService memberService,
                                    ProjectService projectService, PublicationMapper publicationMapper) {
        this.publicationService = publicationService;
        this.memberService = memberService;
        this.projectService = projectService;
        this.publicationMapper = publicationMapper;
    }

    @GetMapping
    public ResponseEntity<List<PublicationResponse>> getAllPublications() {
        List<Publication> publications = publicationService.findAll();
        return ResponseEntity.ok(publicationMapper.toResponseList(publications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationResponse> getPublicationById(@PathVariable Long id) {
        Publication publication = publicationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication", id));
        return ResponseEntity.ok(publicationMapper.toResponse(publication));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PublicationResponse>> searchPublications(@RequestParam String query) {
        List<Publication> publications = publicationService.search(query);
        return ResponseEntity.ok(publicationMapper.toResponseList(publications));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PublicationResponse>> getRecentPublications() {
        List<Publication> publications = publicationService.findRecent();
        return ResponseEntity.ok(publicationMapper.toResponseList(publications));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PublicationResponse>> getPublicationsByAuthor(@PathVariable Long authorId) {
        List<Publication> publications = publicationService.findByAuthor(authorId);
        return ResponseEntity.ok(publicationMapper.toResponseList(publications));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<PublicationResponse>> getPublicationsByProject(@PathVariable Long projectId) {
        List<Publication> publications = publicationService.findByProject(projectId);
        return ResponseEntity.ok(publicationMapper.toResponseList(publications));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'TEACHER')")
    public ResponseEntity<PublicationResponse> createPublication(@Valid @RequestBody PublicationRequest request) {
        Publication publication = publicationMapper.toEntity(request);

        if (request.getAuthorId() != null) {
            Member author = memberService.findById(request.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member", request.getAuthorId()));
            publication.setAuthor(author);
        }

        if (request.getProjectId() != null) {
            Project project = projectService.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project", request.getProjectId()));
            publication.setProject(project);
        }

        Publication savedPublication = publicationService.save(publication);
        return new ResponseEntity<>(publicationMapper.toResponse(savedPublication), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'TEACHER')")
    public ResponseEntity<PublicationResponse> updatePublication(
            @PathVariable Long id,
            @Valid @RequestBody PublicationRequest request) {

        Publication publication = publicationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication", id));

        publicationMapper.updateEntity(publication, request);

        if (request.getAuthorId() != null) {
            Member author = memberService.findById(request.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member", request.getAuthorId()));
            publication.setAuthor(author);
        }

        if (request.getProjectId() != null) {
            Project project = projectService.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project", request.getProjectId()));
            publication.setProject(project);
        }

        Publication updatedPublication = publicationService.save(publication);
        return ResponseEntity.ok(publicationMapper.toResponse(updatedPublication));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'TEACHER')")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        if (publicationService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Publication", id);
        }
        publicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
