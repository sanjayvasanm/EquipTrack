package com.equiptrack.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
 * Booking document representing equipment rental bookings
 */
@Document(collection = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "customer", "equipment", "confirmedBy", "cancelledBy"}, allowGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {

    @Id
    private String id;

    @Indexed(unique = true)
    private String bookingNumber;

    private String customerId;

    private String equipmentId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime actualPickupTime;

    private LocalDateTime actualReturnTime;

    private BookingStatus status = BookingStatus.PENDING;

    private BigDecimal totalAmount;

    private BigDecimal securityDeposit;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private BigDecimal taxAmount = BigDecimal.ZERO;

    private BigDecimal finalAmount;

    private String notes;

    private String customerNotes;

    private String adminNotes;

    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    private String deliveryAddress;

    private Boolean requiresDelivery = false;

    private BigDecimal deliveryFee = BigDecimal.ZERO;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private String cancelledById;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime confirmedAt;

    private String confirmedById;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }

    public enum PaymentStatus {
        UNPAID,
        PARTIALLY_PAID,
        PAID,
        REFUNDED,
        REFUND_PENDING
    }
}
