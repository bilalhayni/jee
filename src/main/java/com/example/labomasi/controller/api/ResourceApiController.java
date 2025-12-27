package com.example.labomasi.controller.api;

import com.example.labomasi.dto.request.ResourceRequest;
import com.example.labomasi.dto.response.ResourceResponse;
import com.example.labomasi.entity.Resource;
import com.example.labomasi.enums.ResourceCategory;
import com.example.labomasi.exception.ResourceNotFoundException;
import com.example.labomasi.mapper.ResourceMapper;
import com.example.labomasi.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceApiController {

    private final ResourceService resourceService;
    private final ResourceMapper resourceMapper;

    public ResourceApiController(ResourceService resourceService, ResourceMapper resourceMapper) {
        this.resourceService = resourceService;
        this.resourceMapper = resourceMapper;
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getAllResources() {
        List<Resource> resources = resourceService.findAll();
        return ResponseEntity.ok(resourceMapper.toResponseList(resources));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getResourceById(@PathVariable Long id) {
        Resource resource = resourceService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource", id));
        return ResponseEntity.ok(resourceMapper.toResponse(resource));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResourceResponse>> searchResources(@RequestParam String query) {
        List<Resource> resources = resourceService.search(query);
        return ResponseEntity.ok(resourceMapper.toResponseList(resources));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ResourceResponse>> getResourcesByCategory(@PathVariable ResourceCategory category) {
        List<Resource> resources = resourceService.findByCategory(category);
        return ResponseEntity.ok(resourceMapper.toResponseList(resources));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<ResourceResponse> createResource(@Valid @RequestBody ResourceRequest request) {
        Resource resource = resourceMapper.toEntity(request);
        Resource savedResource = resourceService.save(resource);
        return new ResponseEntity<>(resourceMapper.toResponse(savedResource), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<ResourceResponse> updateResource(
            @PathVariable Long id,
            @Valid @RequestBody ResourceRequest request) {

        Resource resource = resourceService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource", id));

        resourceMapper.updateEntity(resource, request);
        Resource updatedResource = resourceService.save(resource);

        return ResponseEntity.ok(resourceMapper.toResponse(updatedResource));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        if (resourceService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Resource", id);
        }
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
