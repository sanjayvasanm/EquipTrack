package com.equiptrack.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Maintenance Record document for tracking equipment maintenance
 */
@Document(collection = "maintenance_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecord {

    @Id
    private String id;

    private String equipmentId;

    private MaintenanceType type;

    private LocalDate maintenanceDate;

    private String description;

    private String performedBy;

    private BigDecimal cost;

    private MaintenanceStatus status = MaintenanceStatus.SCHEDULED;

    private LocalDate nextMaintenanceDate;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    public enum MaintenanceType {
        ROUTINE,
        REPAIR,
        INSPECTION,
        EMERGENCY,
        PREVENTIVE,
        CLEANING
    }

    public enum MaintenanceStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
