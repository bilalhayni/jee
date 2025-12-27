package com.example.labomasi.repository;

import com.example.labomasi.entity.Project;
import com.example.labomasi.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(ProjectStatus status);

    long countByStatus(ProjectStatus status);

    List<Project> findByLeadId(Long leadId);

    List<Project> findTop5ByOrderByCreatedAtDesc();

    List<Project> findByTitleContainingIgnoreCase(String title);

    List<Project> findAllByOrderByCreatedAtDesc();

}