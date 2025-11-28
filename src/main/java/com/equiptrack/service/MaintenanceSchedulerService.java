package com.equiptrack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equiptrack.model.Equipment;
import com.equiptrack.repository.EquipmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduled service to automatically return equipment from MAINTENANCE to AVAILABLE
 * after the maintenance period is complete
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MaintenanceSchedulerService {

    private final EquipmentRepository equipmentRepository;

    /**
     * Runs every hour to check for equipment that has completed maintenance
     * and should be returned to AVAILABLE status
     */
    @Scheduled(fixedRate = 3600000) // Run every hour (3600000 ms = 1 hour)
    @Transactional
    public void checkMaintenanceCompletion() {
        log.debug("Running maintenance completion check...");
        
        try {
            // Find all equipment in MAINTENANCE status
            List<Equipment> maintenanceEquipment = equipmentRepository.findByStatus(Equipment.EquipmentStatus.MAINTENANCE);
            
            LocalDateTime now = LocalDateTime.now();
            int updatedCount = 0;
            
            for (Equipment equipment : maintenanceEquipment) {
                // Check if maintenance period is over
                if (equipment.getNextMaintenanceDate() != null && 
                    equipment.getNextMaintenanceDate().isBefore(now)) {
                    
                    log.info("Maintenance period complete for equipment: {} ({}). Returning to AVAILABLE status.",
                            equipment.getName(), equipment.getEquipmentCode());
                    
                    equipment.setStatus(Equipment.EquipmentStatus.AVAILABLE);
                    equipmentRepository.save(equipment);
                    updatedCount++;
                }
            }
            
            if (updatedCount > 0) {
                log.info("Returned {} equipment(s) from MAINTENANCE to AVAILABLE status", updatedCount);
            }
            
        } catch (Exception e) {
            log.error("Error during maintenance completion check", e);
        }
    }
}
