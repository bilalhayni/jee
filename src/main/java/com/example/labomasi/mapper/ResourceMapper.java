package com.example.labomasi.mapper;

import com.example.labomasi.dto.request.ResourceRequest;
import com.example.labomasi.dto.response.ResourceResponse;
import com.example.labomasi.entity.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResourceMapper {

    public ResourceResponse toResponse(Resource resource) {
        if (resource == null) {
            return null;
        }

        return ResourceResponse.builder()
                .id(resource.getId())
                .name(resource.getName())
                .description(resource.getDescription())
                .category(resource.getCategory())
                .categoryDisplayName(resource.getCategory() != null ? resource.getCategory().getDisplayName() : null)
                .location(resource.getLocation())
                .quantity(resource.getQuantity())
                .available(resource.isAvailable())
                .serialNumber(resource.getSerialNumber())
                .purchaseDate(resource.getPurchaseDate())
                .conditionStatus(resource.getConditionStatus())
                .createdAt(resource.getCreatedAt())
                .updatedAt(resource.getUpdatedAt())
                .build();
    }

    public List<ResourceResponse> toResponseList(List<Resource> resources) {
        if (resources == null) {
            return null;
        }
        return resources.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Resource toEntity(ResourceRequest request) {
        if (request == null) {
            return null;
        }

        return Resource.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .location(request.getLocation())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 1)
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .serialNumber(request.getSerialNumber())
                .purchaseDate(request.getPurchaseDate())
                .conditionStatus(request.getConditionStatus())
                .build();
    }

    public void updateEntity(Resource resource, ResourceRequest request) {
        if (resource == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            resource.setName(request.getName());
        }
        if (request.getDescription() != null) {
            resource.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            resource.setCategory(request.getCategory());
        }
        if (request.getLocation() != null) {
            resource.setLocation(request.getLocation());
        }
        if (request.getQuantity() != null) {
            resource.setQuantity(request.getQuantity());
        }
        if (request.getAvailable() != null) {
            resource.setAvailable(request.getAvailable());
        }
        if (request.getSerialNumber() != null) {
            resource.setSerialNumber(request.getSerialNumber());
        }
        if (request.getPurchaseDate() != null) {
            resource.setPurchaseDate(request.getPurchaseDate());
        }
        if (request.getConditionStatus() != null) {
            resource.setConditionStatus(request.getConditionStatus());
        }
    }
}
