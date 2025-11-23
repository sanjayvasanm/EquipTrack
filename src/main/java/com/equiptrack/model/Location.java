package com.equiptrack.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Location document for equipment storage locations
 */
@Document(collection = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String code;

    private LocationType type;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String country = "USA";

    private Double latitude;

    private Double longitude;

    private String phoneNumber;

    private String email;

    private String operatingHours;

    private Boolean isActive = true;

    private Boolean supportsPickup = true;

    private Boolean supportsDelivery = true;

    @CreatedDate
    private java.time.LocalDateTime createdAt;

    public enum LocationType {
        WAREHOUSE,
        SERVICE_CENTER,
        RETAIL_STORE,
        DISTRIBUTION_CENTER
    }
}
