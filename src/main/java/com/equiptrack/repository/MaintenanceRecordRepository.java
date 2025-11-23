package com.equiptrack.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.MaintenanceRecord;

@Repository
public interface MaintenanceRecordRepository extends MongoRepository<MaintenanceRecord, String> {
    
    List<MaintenanceRecord> findByEquipmentId(String equipmentId);
    
    List<MaintenanceRecord> findByStatus(MaintenanceRecord.MaintenanceStatus status);
    
    List<MaintenanceRecord> findByType(MaintenanceRecord.MaintenanceType type);
    
    @Query(value = "{ 'equipmentId': ?0 }", sort = "{ 'maintenanceDate': -1 }")
    List<MaintenanceRecord> findRecentMaintenanceByEquipment(String equipmentId);
    
    @Query("{ 'nextMaintenanceDate': { $lte: ?0 }, 'status': { $ne: 'COMPLETED' } }")
    List<MaintenanceRecord> findUpcomingMaintenance(LocalDate date);
    
    @Query("{ 'maintenanceDate': { $gte: ?0, $lte: ?1 } }")
    List<MaintenanceRecord> findMaintenanceInDateRange(LocalDate startDate, LocalDate endDate);
}
