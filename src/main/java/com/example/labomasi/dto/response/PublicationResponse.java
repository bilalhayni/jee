package com.example.labomasi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationResponse {

    private Long id;

    private String title;

    private String abstractText;

    private String authors;

    private String journal;

    private String conference;

    private String doi;

    private String url;

    private LocalDate publicationDate;

    private String publicationType;

    private Integer citations;

    private MemberResponse author;

    private ProjectResponse project;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
