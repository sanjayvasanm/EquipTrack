package com.equiptrack.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.Equipment;

@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, String> {
    
    Optional<Equipment> findByEquipmentCode(String equipmentCode);
    
    List<Equipment> findByStatus(Equipment.EquipmentStatus status);
    
    List<Equipment> findByCategoryId(String categoryId);
    
    List<Equipment> findByLocationId(String locationId);
    
    List<Equipment> findByIsActiveTrue();
    
    List<Equipment> findByIsFeaturedTrue();
    
    @Query("{ 'status': 'AVAILABLE', 'isActive': true }")
    List<Equipment> findAvailableEquipment();
    
    @Query("{ 'categoryId': ?0, 'status': 'AVAILABLE', 'isActive': true }")
    List<Equipment> findAvailableEquipmentByCategory(String categoryId);
    
    @Query("{ 'locationId': ?0, 'status': 'AVAILABLE', 'isActive': true }")
    List<Equipment> findAvailableEquipmentByLocation(String locationId);
    
    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'manufacturer': { $regex: ?0, $options: 'i' } } ] }")
    List<Equipment> searchEquipment(String keyword);
    
    @Query("{ 'dailyRate': { $gte: ?0, $lte: ?1 } }")
    List<Equipment> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query(value = "{ 'status': ?0 }", count = true)
    Long countByStatus(Equipment.EquipmentStatus status);
    
    @Query("{ 'categoryId': ?0, 'status': ?1 }")
    List<Equipment> findByCategoryAndStatus(String categoryId, Equipment.EquipmentStatus status);
}
