package com.equiptrack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.Location;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
    
    Optional<Location> findByName(String name);
    
    Optional<Location> findByCode(String code);
    
    List<Location> findByIsActiveTrue();
    
    List<Location> findByType(Location.LocationType type);
    
    List<Location> findByCity(String city);
    
    List<Location> findByState(String state);
    
    @Query("{ 'isActive': true, 'supportsPickup': true }")
    List<Location> findPickupLocations();
    
    @Query("{ 'isActive': true, 'supportsDelivery': true }")
    List<Location> findDeliveryLocations();
}
