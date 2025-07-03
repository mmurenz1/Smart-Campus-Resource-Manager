package com.campus.resourcemanager.repository;

import com.campus.resourcemanager.model.Resource;
import com.campus.resourcemanager.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    // Find resources by type
    List<Resource> findByType(ResourceType type);
    
    // Find available resources
    List<Resource> findByIsAvailableTrue();
    
    // Find resources by location
    List<Resource> findByLocationContainingIgnoreCase(String location);
    
    // Find resources by name (case insensitive)
    List<Resource> findByNameContainingIgnoreCase(String name);
    
    // Find available resources by type
    List<Resource> findByTypeAndIsAvailableTrue(ResourceType type);
    
    // Find resources with capacity greater than or equal to specified value
    List<Resource> findByCapacityGreaterThanEqual(Integer minCapacity);
    
    // Find available resources with minimum capacity
    List<Resource> findByIsAvailableTrueAndCapacityGreaterThanEqual(Integer minCapacity);
    
    // Custom query to find resources by multiple criteria
    @Query("SELECT r FROM Resource r WHERE " +
           "(:type IS NULL OR r.type = :type) AND " +
           "(:minCapacity IS NULL OR r.capacity >= :minCapacity) AND " +
           "(:location IS NULL OR LOWER(r.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:availableOnly = false OR r.isAvailable = true)")
    List<Resource> findResourcesByCriteria(
        @Param("type") ResourceType type,
        @Param("minCapacity") Integer minCapacity,
        @Param("location") String location,
        @Param("availableOnly") boolean availableOnly
    );
    
    // Count available resources by type
    @Query("SELECT COUNT(r) FROM Resource r WHERE r.type = :type AND r.isAvailable = true")
    Long countAvailableResourcesByType(@Param("type") ResourceType type);
    
    // Count available resources
    Long countByIsAvailableTrue();
    
    // Find most popular resource types (most bookings - we'll implement this later)
    @Query("SELECT r.type, COUNT(r) as resourceCount FROM Resource r GROUP BY r.type ORDER BY resourceCount DESC")
    List<Object[]> findResourceCountByType();
}