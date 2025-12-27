package com.example.labomasi.service;

import com.example.labomasi.entity.Project;
import com.example.labomasi.enums.ProjectStatus;
import com.example.labomasi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> findAll() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findByIdWithDetails(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project update(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setStatus(projectDetails.getStatus());
        project.setBudget(projectDetails.getBudget());
        project.setFundingSource(projectDetails.getFundingSource());
        project.setLead(projectDetails.getLead());

        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Project> findByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }

    public long count() {
        return projectRepository.count();
    }

    public long countByStatus(ProjectStatus status) {
        return projectRepository.countByStatus(status);
    }

    public List<Project> findRecent() {
        return projectRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public List<Project> search(String keyword) {
        return projectRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Project> findByMember(Long memberId) {
        return projectRepository.findByMemberId(memberId);
    }
}