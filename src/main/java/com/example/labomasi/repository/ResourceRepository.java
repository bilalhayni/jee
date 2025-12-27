package com.example.labomasi.repository;

import com.example.labomasi.entity.Resource;
import com.example.labomasi.enums.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByCategory(ResourceCategory category);

    List<Resource> findByAvailableTrue();

    long countByAvailableTrue();

    List<Resource> findByNameContainingIgnoreCase(String name);

    List<Resource> findAllByOrderByCreatedAtDesc();
}