package com.equiptrack.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Equipment document representing rental equipment items
 */
@Document(collection = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "category", "location", "bookings", "maintenanceRecords", "additionalImages", "specifications"}, allowGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Equipment {

    @Id
    private String id;

    @Indexed(unique = true)
    private String equipmentCode;

    private String name;

    private String description;

    private String categoryId;

    private String locationId;

    private BigDecimal dailyRate;

    private BigDecimal weeklyRate;

    private BigDecimal monthlyRate;

    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    private EquipmentCondition condition = EquipmentCondition.EXCELLENT;

    private String manufacturer;

    private String model;

    private String serialNumber;

    private Integer yearOfManufacture;

    private String imageUrl;

    private List<String> additionalImages = new ArrayList<>();

    private Map<String, String> specifications = new HashMap<>();

    private Boolean isActive = true;

    private Boolean isFeatured = false;

    private Integer minimumRentalDays = 1;

    private Integer maximumRentalDays = 365;

    private BigDecimal securityDeposit;

    private String terms;

    private Double averageRating = 0.0;

    private Integer totalReviews = 0;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastMaintenanceDate;

    private LocalDateTime nextMaintenanceDate;

    public enum EquipmentStatus {
        AVAILABLE,
        RENTED,
        MAINTENANCE,
        RESERVED,
        OUT_OF_SERVICE,
        RETIRED
    }

    public enum EquipmentCondition {
        EXCELLENT,
        GOOD,
        FAIR,
        POOR,
        NEEDS_REPAIR
    }
}
