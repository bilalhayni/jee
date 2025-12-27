package com.example.labomasi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 500, message = "Title must be between 3 and 500 characters")
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

    private Long authorId;

    private Long projectId;
}
