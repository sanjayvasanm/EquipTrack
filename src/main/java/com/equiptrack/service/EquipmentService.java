package com.equiptrack.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equiptrack.model.Equipment;
import com.equiptrack.repository.EquipmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing equipment operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getAllEquipment() {
        log.info("Fetching all equipment");
        return equipmentRepository.findAll();
    }

    public List<Equipment> getAvailableEquipment() {
        log.info("Fetching available equipment");
        return equipmentRepository.findAvailableEquipment();
    }

    public List<Equipment> getFeaturedEquipment() {
        log.info("Fetching featured equipment");
        return equipmentRepository.findByIsFeaturedTrue();
    }

    public Optional<Equipment> getEquipmentById(String id) {
        log.info("Fetching equipment with ID: {}", id);
        return equipmentRepository.findById(id);
    }

    public Optional<Equipment> getEquipmentByCode(String code) {
        log.info("Fetching equipment with code: {}", code);
        return equipmentRepository.findByEquipmentCode(code);
    }

    public List<Equipment> getEquipmentByCategory(String categoryId) {
        log.info("Fetching equipment for category ID: {}", categoryId);
        return equipmentRepository.findByCategoryId(categoryId);
    }

    public List<Equipment> getAvailableEquipmentByCategory(String categoryId) {
        log.info("Fetching available equipment for category ID: {}", categoryId);
        return equipmentRepository.findAvailableEquipmentByCategory(categoryId);
    }

    public List<Equipment> getEquipmentByLocation(String locationId) {
        log.info("Fetching equipment for location ID: {}", locationId);
        return equipmentRepository.findByLocationId(locationId);
    }

    public List<Equipment> searchEquipment(String keyword) {
        log.info("Searching equipment with keyword: {}", keyword);
        return equipmentRepository.searchEquipment(keyword);
    }

    public List<Equipment> getEquipmentByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching equipment in price range: {} - {}", minPrice, maxPrice);
        return equipmentRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Equipment> getEquipmentByStatus(Equipment.EquipmentStatus status) {
        log.info("Fetching equipment with status: {}", status);
        return equipmentRepository.findByStatus(status);
    }

    public Equipment createEquipment(Equipment equipment) {
        log.info("Creating new equipment: {}", equipment.getName());
        
        // Generate equipment code if not provided
        if (equipment.getEquipmentCode() == null || equipment.getEquipmentCode().isEmpty()) {
            equipment.setEquipmentCode(generateEquipmentCode());
        }
        
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(String id, Equipment equipmentDetails) {
        log.info("Updating equipment with ID: {}", id);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with ID: " + id));

        equipment.setName(equipmentDetails.getName());
        equipment.setDescription(equipmentDetails.getDescription());
        equipment.setCategoryId(equipmentDetails.getCategoryId());
        equipment.setLocationId(equipmentDetails.getLocationId());
        equipment.setDailyRate(equipmentDetails.getDailyRate());
        equipment.setWeeklyRate(equipmentDetails.getWeeklyRate());
        equipment.setMonthlyRate(equipmentDetails.getMonthlyRate());
        equipment.setManufacturer(equipmentDetails.getManufacturer());
        equipment.setModel(equipmentDetails.getModel());
        equipment.setImageUrl(equipmentDetails.getImageUrl());
        
        return equipmentRepository.save(equipment);
    }

    public void updateEquipmentStatus(String id, Equipment.EquipmentStatus status) {
        log.info("Updating equipment {} status to: {}", id, status);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with ID: " + id));
        
        equipment.setStatus(status);
        equipmentRepository.save(equipment);
    }

    public void deleteEquipment(String id) {
        log.info("Deleting equipment with ID: {}", id);
        
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with ID: " + id));
        
        // Prevent deletion of equipment that is currently rented
        if (equipment.getStatus() == Equipment.EquipmentStatus.RENTED) {
            throw new RuntimeException("Cannot delete equipment that is currently rented. Please wait until the rental is completed.");
        }
        
        equipment.setIsActive(false);
        equipment.setStatus(Equipment.EquipmentStatus.RETIRED);
        equipmentRepository.save(equipment);
        
        log.info("Equipment {} marked as RETIRED and inactive", equipment.getEquipmentCode());
    }

    public Long getEquipmentCountByStatus(Equipment.EquipmentStatus status) {
        return equipmentRepository.countByStatus(status);
    }

    private String generateEquipmentCode() {
        long count = equipmentRepository.count() + 1;
        return String.format("EQ%06d", count);
    }

    public boolean isEquipmentAvailable(String equipmentId) {
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        return equipment.isPresent() && 
               equipment.get().getStatus() == Equipment.EquipmentStatus.AVAILABLE &&
               equipment.get().getIsActive();
    }
}
