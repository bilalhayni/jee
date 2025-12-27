package com.example.labomasi.repository;

import com.example.labomasi.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query("SELECT p FROM Publication p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.project WHERE p.author.id = :authorId ORDER BY p.publicationDate DESC")
    List<Publication> findByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT p FROM Publication p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.project WHERE p.project.id = :projectId ORDER BY p.publicationDate DESC")
    List<Publication> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(p) FROM Publication p WHERE YEAR(p.publicationDate) = YEAR(CURRENT_DATE)")
    long countThisYear();

    @Query("SELECT p FROM Publication p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.project ORDER BY p.publicationDate DESC LIMIT 5")
    List<Publication> findTop5ByOrderByPublicationDateDesc();

    @Query("SELECT p FROM Publication p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.project WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY p.publicationDate DESC")
    List<Publication> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT p FROM Publication p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.project ORDER BY p.publicationDate DESC")
    List<Publication> findAllByOrderByPublicationDateDesc();

    @Query("SELECT p FROM Publication p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.project WHERE p.id = :id")
    Optional<Publication> findByIdWithDetails(@Param("id") Long id);
}