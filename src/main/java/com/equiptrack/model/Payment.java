package com.equiptrack.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payment document for tracking payment transactions
 */
@Document(collection = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

    @Id
    private String id;

    @Indexed(unique = true)
    private String transactionId;

    private String bookingId;

    private String userId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status = PaymentStatus.PENDING;

    private PaymentType type;

    private String description;

    private String stripePaymentIntentId;

    private String stripeChargeId;

    private String failureReason;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    private LocalDateTime refundedAt;

    private BigDecimal refundedAmount;

    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        BANK_TRANSFER,
        CASH,
        PAYPAL,
        STRIPE,
        OTHER
    }

    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        REFUNDED,
        CANCELLED
    }

    public enum PaymentType {
        BOOKING_PAYMENT,
        SECURITY_DEPOSIT,
        ADDITIONAL_CHARGES,
        REFUND,
        LATE_FEE,
        DAMAGE_FEE
    }
}
