package com.example.labomasi.service;

import com.example.labomasi.entity.Publication;
import com.example.labomasi.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicationService {

    private final PublicationRepository publicationRepository;

    public List<Publication> findAll() {
        return publicationRepository.findAllByOrderByPublicationDateDesc();
    }

    public Optional<Publication> findById(Long id) {
        return publicationRepository.findById(id);
    }

    public Publication save(Publication publication) {
        return publicationRepository.save(publication);
    }

    public Publication update(Long id, Publication publicationDetails) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));

        publication.setTitle(publicationDetails.getTitle());
        publication.setAbstractText(publicationDetails.getAbstractText());
        publication.setAuthors(publicationDetails.getAuthors());
        publication.setJournal(publicationDetails.getJournal());
        publication.setConference(publicationDetails.getConference());
        publication.setDoi(publicationDetails.getDoi());
        publication.setUrl(publicationDetails.getUrl());
        publication.setPublicationDate(publicationDetails.getPublicationDate());
        publication.setPublicationType(publicationDetails.getPublicationType());
        publication.setCitations(publicationDetails.getCitations());
        publication.setAuthor(publicationDetails.getAuthor());
        publication.setProject(publicationDetails.getProject());

        return publicationRepository.save(publication);
    }

    public void delete(Long id) {
        publicationRepository.deleteById(id);
    }

    public long count() {
        return publicationRepository.count();
    }

    public long countThisYear() {
        return publicationRepository.countThisYear();
    }

    public List<Publication> findRecent() {
        return publicationRepository.findTop5ByOrderByPublicationDateDesc();
    }

    public List<Publication> search(String keyword) {
        return publicationRepository.findByTitleContainingIgnoreCase(keyword);
    }
}