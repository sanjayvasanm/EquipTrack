package com.equiptrack.service;

import com.equiptrack.model.Location;
import com.equiptrack.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing locations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getAllActiveLocations() {
        return locationRepository.findByIsActiveTrue();
    }

    public List<Location> getPickupLocations() {
        return locationRepository.findPickupLocations();
    }

    public List<Location> getDeliveryLocations() {
        return locationRepository.findDeliveryLocations();
    }

    public Optional<Location> getLocationById(String id) {
        return locationRepository.findById(id);
    }

    public Location createLocation(Location location) {
        log.info("Creating new location: {}", location.getName());
        return locationRepository.save(location);
    }

    public Location updateLocation(String id, Location locationDetails) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        
        location.setName(locationDetails.getName());
        location.setAddress(locationDetails.getAddress());
        location.setCity(locationDetails.getCity());
        location.setState(locationDetails.getState());
        location.setPhoneNumber(locationDetails.getPhoneNumber());
        
        return locationRepository.save(location);
    }

    public void deleteLocation(String id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        
        location.setIsActive(false);
        locationRepository.save(location);
    }
}
