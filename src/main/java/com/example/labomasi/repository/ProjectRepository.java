package com.example.labomasi.repository;

import com.example.labomasi.entity.Project;
import com.example.labomasi.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.lead LEFT JOIN FETCH p.members WHERE p.id = :id")
    Optional<Project> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.lead WHERE p.status = :status")
    List<Project> findByStatus(@Param("status") ProjectStatus status);

    long countByStatus(ProjectStatus status);

    List<Project> findByLeadId(Long leadId);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.lead ORDER BY p.createdAt DESC")
    List<Project> findAllByOrderByCreatedAtDesc();

    default List<Project> findTop5ByOrderByCreatedAtDesc() {
        return findAllByOrderByCreatedAtDesc().stream().limit(5).toList();
    }

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.lead WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Project> findByTitleContainingIgnoreCase(@Param("title") String title);

    // Find projects where the member is either the lead or a participant
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.lead LEFT JOIN p.members m WHERE p.lead.id = :memberId OR m.id = :memberId ORDER BY p.createdAt DESC")
    List<Project> findByMemberId(@Param("memberId") Long memberId);
}