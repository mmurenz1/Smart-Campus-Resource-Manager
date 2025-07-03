package com.campus.resourcemanager.service;

import com.campus.resourcemanager.model.Resource;
import com.campus.resourcemanager.model.ResourceType;
import com.campus.resourcemanager.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {
    
    private final ResourceRepository resourceRepository;
    
    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }
    
    // Create a new resource
    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }
    
    // Get all resources
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
    
    // Get resource by ID
    public Optional<Resource> getResourceById(Long id) {
        return resourceRepository.findById(id);
    }
    
    // Update a resource
    public Resource updateResource(Long id, Resource updatedResource) {
        return resourceRepository.findById(id)
            .map(resource -> {
                resource.setName(updatedResource.getName());
                resource.setType(updatedResource.getType());
                resource.setCapacity(updatedResource.getCapacity());
                resource.setDescription(updatedResource.getDescription());
                resource.setLocation(updatedResource.getLocation());
                resource.setEquipment(updatedResource.getEquipment());
                resource.setIsAvailable(updatedResource.getIsAvailable());
                return resourceRepository.save(resource);
            })
            .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }
    
    // Delete a resource
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new RuntimeException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }
    
    // Get available resources
    public List<Resource> getAvailableResources() {
        return resourceRepository.findByIsAvailableTrue();
    }
    
    // Get resources by type
    public List<Resource> getResourcesByType(ResourceType type) {
        return resourceRepository.findByType(type);
    }
    
    // Get available resources by type
    public List<Resource> getAvailableResourcesByType(ResourceType type) {
        return resourceRepository.findByTypeAndIsAvailableTrue(type);
    }
    
    // Search resources by location
    public List<Resource> searchResourcesByLocation(String location) {
        return resourceRepository.findByLocationContainingIgnoreCase(location);
    }
    
    // Search resources by name
    public List<Resource> searchResourcesByName(String name) {
        return resourceRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Get resources with minimum capacity
    public List<Resource> getResourcesWithMinCapacity(Integer minCapacity) {
        return resourceRepository.findByCapacityGreaterThanEqual(minCapacity);
    }
    
    // Get available resources with minimum capacity
    public List<Resource> getAvailableResourcesWithMinCapacity(Integer minCapacity) {
        return resourceRepository.findByIsAvailableTrueAndCapacityGreaterThanEqual(minCapacity);
    }
    
    // Advanced search with multiple criteria
    public List<Resource> searchResources(ResourceType type, Integer minCapacity, 
                                        String location, boolean availableOnly) {
        return resourceRepository.findResourcesByCriteria(type, minCapacity, location, availableOnly);
    }
    
    // Toggle resource availability
    public Resource toggleResourceAvailability(Long id) {
        return resourceRepository.findById(id)
            .map(resource -> {
                resource.setIsAvailable(!resource.getIsAvailable());
                return resourceRepository.save(resource);
            })
            .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }
    
    // Get resource statistics
    public Long getAvailableResourceCount() {
        return resourceRepository.countByIsAvailableTrue();
    }
    
    public Long getAvailableResourceCountByType(ResourceType type) {
        return resourceRepository.countAvailableResourcesByType(type);
    }
}