package com.campus.resourcemanager.controller;

import com.campus.resourcemanager.model.Resource;
import com.campus.resourcemanager.model.ResourceType;
import com.campus.resourcemanager.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*") // Allow frontend to connect
public class ResourceController {
    
    private final ResourceService resourceService;
    
    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
    
    // GET /api/resources - Get all resources
    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources(
            @RequestParam(required = false) ResourceType type,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "false") boolean availableOnly) {
        
        List<Resource> resources;
        
        // If any search parameters provided, use advanced search
        if (type != null || minCapacity != null || location != null || availableOnly) {
            resources = resourceService.searchResources(type, minCapacity, location, availableOnly);
        } else {
            resources = resourceService.getAllResources();
        }
        
        return ResponseEntity.ok(resources);
    }
    
    // GET /api/resources/{id} - Get resource by ID
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
        Optional<Resource> resource = resourceService.getResourceById(id);
        return resource.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/resources - Create new resource
    @PostMapping
    public ResponseEntity<Resource> createResource(@Valid @RequestBody Resource resource) {
        try {
            Resource createdResource = resourceService.createResource(resource);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // PUT /api/resources/{id} - Update resource
    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(@PathVariable Long id, 
                                                  @Valid @RequestBody Resource resource) {
        try {
            Resource updatedResource = resourceService.updateResource(id, resource);
            return ResponseEntity.ok(updatedResource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // DELETE /api/resources/{id} - Delete resource
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        try {
            resourceService.deleteResource(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/resources/available - Get only available resources
    @GetMapping("/available")
    public ResponseEntity<List<Resource>> getAvailableResources() {
        List<Resource> resources = resourceService.getAvailableResources();
        return ResponseEntity.ok(resources);
    }
    
    // GET /api/resources/type/{type} - Get resources by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Resource>> getResourcesByType(@PathVariable ResourceType type) {
        List<Resource> resources = resourceService.getResourcesByType(type);
        return ResponseEntity.ok(resources);
    }
    
    // GET /api/resources/available/type/{type} - Get available resources by type
    @GetMapping("/available/type/{type}")
    public ResponseEntity<List<Resource>> getAvailableResourcesByType(@PathVariable ResourceType type) {
        List<Resource> resources = resourceService.getAvailableResourcesByType(type);
        return ResponseEntity.ok(resources);
    }
    
    // GET /api/resources/search/location - Search by location
    @GetMapping("/search/location")
    public ResponseEntity<List<Resource>> searchByLocation(@RequestParam String location) {
        List<Resource> resources = resourceService.searchResourcesByLocation(location);
        return ResponseEntity.ok(resources);
    }
    
    // GET /api/resources/search/name - Search by name
    @GetMapping("/search/name")
    public ResponseEntity<List<Resource>> searchByName(@RequestParam String name) {
        List<Resource> resources = resourceService.searchResourcesByName(name);
        return ResponseEntity.ok(resources);
    }
    
    // GET /api/resources/capacity/{minCapacity} - Get resources with minimum capacity
    @GetMapping("/capacity/{minCapacity}")
    public ResponseEntity<List<Resource>> getResourcesWithMinCapacity(@PathVariable Integer minCapacity) {
        List<Resource> resources = resourceService.getResourcesWithMinCapacity(minCapacity);
        return ResponseEntity.ok(resources);
    }
    
    // PUT /api/resources/{id}/toggle-availability - Toggle resource availability
    @PutMapping("/{id}/toggle-availability")
    public ResponseEntity<Resource> toggleResourceAvailability(@PathVariable Long id) {
        try {
            Resource resource = resourceService.toggleResourceAvailability(id);
            return ResponseEntity.ok(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/resources/stats/count - Get total available resource count
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getAvailableResourceCount() {
        Long count = resourceService.getAvailableResourceCount();
        return ResponseEntity.ok(count);
    }
    
    // GET /api/resources/stats/count/type/{type} - Get available count by type
    @GetMapping("/stats/count/type/{type}")
    public ResponseEntity<Long> getAvailableResourceCountByType(@PathVariable ResourceType type) {
        Long count = resourceService.getAvailableResourceCountByType(type);
        return ResponseEntity.ok(count);
    }
    
    // GET /api/resources/types - Get all resource types
    @GetMapping("/types")
    public ResponseEntity<ResourceType[]> getAllResourceTypes() {
        return ResponseEntity.ok(ResourceType.values());
    }
}