package com.equiptrack.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notification document for user notifications
 */
@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private String userId;

    private NotificationType type;

    private String title;

    private String message;

    private String link;

    private Boolean isRead = false;

    private LocalDateTime readAt;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum NotificationType {
        BOOKING_CONFIRMED,
        BOOKING_CANCELLED,
        PAYMENT_RECEIVED,
        PAYMENT_FAILED,
        EQUIPMENT_AVAILABLE,
        REMINDER,
        MAINTENANCE_SCHEDULED,
        SYSTEM_ALERT,
        PROMOTION,
        OTHER
    }
}
