package com.example.labomasi.dto.response;

import com.example.labomasi.enums.ResourceCategory;
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
public class ResourceResponse {

    private Long id;

    private String name;

    private String description;

    private ResourceCategory category;

    private String categoryDisplayName;

    private String location;

    private Integer quantity;

    private boolean available;

    private String serialNumber;

    private LocalDate purchaseDate;

    private String conditionStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
