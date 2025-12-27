package com.example.labomasi.mapper;

import com.example.labomasi.dto.request.PublicationRequest;
import com.example.labomasi.dto.response.PublicationResponse;
import com.example.labomasi.entity.Publication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PublicationMapper {

    private final MemberMapper memberMapper;
    private final ProjectMapper projectMapper;

    public PublicationMapper(MemberMapper memberMapper, ProjectMapper projectMapper) {
        this.memberMapper = memberMapper;
        this.projectMapper = projectMapper;
    }

    public PublicationResponse toResponse(Publication publication) {
        if (publication == null) {
            return null;
        }

        return PublicationResponse.builder()
                .id(publication.getId())
                .title(publication.getTitle())
                .abstractText(publication.getAbstractText())
                .authors(publication.getAuthors())
                .journal(publication.getJournal())
                .conference(publication.getConference())
                .doi(publication.getDoi())
                .url(publication.getUrl())
                .publicationDate(publication.getPublicationDate())
                .publicationType(publication.getPublicationType())
                .citations(publication.getCitations())
                .author(memberMapper.toResponse(publication.getAuthor()))
                .project(projectMapper.toResponse(publication.getProject()))
                .createdAt(publication.getCreatedAt())
                .updatedAt(publication.getUpdatedAt())
                .build();
    }

    public List<PublicationResponse> toResponseList(List<Publication> publications) {
        if (publications == null) {
            return null;
        }
        return publications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Publication toEntity(PublicationRequest request) {
        if (request == null) {
            return null;
        }

        return Publication.builder()
                .title(request.getTitle())
                .abstractText(request.getAbstractText())
                .authors(request.getAuthors())
                .journal(request.getJournal())
                .conference(request.getConference())
                .doi(request.getDoi())
                .url(request.getUrl())
                .publicationDate(request.getPublicationDate())
                .publicationType(request.getPublicationType())
                .citations(request.getCitations() != null ? request.getCitations() : 0)
                .build();
    }

    public void updateEntity(Publication publication, PublicationRequest request) {
        if (publication == null || request == null) {
            return;
        }

        if (request.getTitle() != null) {
            publication.setTitle(request.getTitle());
        }
        if (request.getAbstractText() != null) {
            publication.setAbstractText(request.getAbstractText());
        }
        if (request.getAuthors() != null) {
            publication.setAuthors(request.getAuthors());
        }
        if (request.getJournal() != null) {
            publication.setJournal(request.getJournal());
        }
        if (request.getConference() != null) {
            publication.setConference(request.getConference());
        }
        if (request.getDoi() != null) {
            publication.setDoi(request.getDoi());
        }
        if (request.getUrl() != null) {
            publication.setUrl(request.getUrl());
        }
        if (request.getPublicationDate() != null) {
            publication.setPublicationDate(request.getPublicationDate());
        }
        if (request.getPublicationType() != null) {
            publication.setPublicationType(request.getPublicationType());
        }
        if (request.getCitations() != null) {
            publication.setCitations(request.getCitations());
        }
    }
}
