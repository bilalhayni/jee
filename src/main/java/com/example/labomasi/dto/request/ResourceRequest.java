package com.example.labomasi.dto.request;

import com.example.labomasi.enums.ResourceCategory;
import jakarta.validation.constraints.Min;
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
public class ResourceRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    private String description;

    private ResourceCategory category;

    private String location;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    private Boolean available;

    private String serialNumber;

    private LocalDate purchaseDate;

    private String conditionStatus;
}
