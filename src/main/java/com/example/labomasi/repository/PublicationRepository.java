package com.example.labomasi.repository;

import com.example.labomasi.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findByAuthorId(Long authorId);

    List<Publication> findByProjectId(Long projectId);

    @Query("SELECT COUNT(p) FROM Publication p WHERE YEAR(p.publicationDate) = YEAR(CURRENT_DATE)")
    long countThisYear();

    List<Publication> findTop5ByOrderByPublicationDateDesc();

    List<Publication> findByTitleContainingIgnoreCase(String title);

    List<Publication> findAllByOrderByPublicationDateDesc();
}