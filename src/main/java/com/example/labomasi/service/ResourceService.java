package com.example.labomasi.service;

import com.example.labomasi.entity.Resource;
import com.example.labomasi.enums.ResourceCategory;
import com.example.labomasi.exception.ResourceNotFoundException;
import com.example.labomasi.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public List<Resource> findAll() {
        return resourceRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Resource> findById(Long id) {
        return resourceRepository.findById(id);
    }

    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource update(Long id, Resource resourceDetails) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource", id));

        resource.setName(resourceDetails.getName());
        resource.setDescription(resourceDetails.getDescription());
        resource.setCategory(resourceDetails.getCategory());
        resource.setLocation(resourceDetails.getLocation());
        resource.setQuantity(resourceDetails.getQuantity());
        resource.setAvailable(resourceDetails.isAvailable());
        resource.setSerialNumber(resourceDetails.getSerialNumber());
        resource.setPurchaseDate(resourceDetails.getPurchaseDate());
        resource.setConditionStatus(resourceDetails.getConditionStatus());

        return resourceRepository.save(resource);
    }

    public void delete(Long id) {
        resourceRepository.deleteById(id);
    }

    public long count() {
        return resourceRepository.count();
    }

    public long countAvailable() {
        return resourceRepository.countByAvailableTrue();
    }

    public List<Resource> findByCategory(ResourceCategory category) {
        return resourceRepository.findByCategory(category);
    }

    public List<Resource> search(String keyword) {
        return resourceRepository.findByNameContainingIgnoreCase(keyword);
    }
}