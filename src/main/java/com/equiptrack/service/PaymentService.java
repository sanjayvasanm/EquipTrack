package com.equiptrack.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equiptrack.model.Booking;
import com.equiptrack.model.Payment;
import com.equiptrack.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing payment operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> getAllPayments() {
        log.info("Fetching all payments");
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(String id) {
        log.info("Fetching payment with ID: {}", id);
        return paymentRepository.findById(id);
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        log.info("Fetching payment with transaction ID: {}", transactionId);
        return paymentRepository.findByTransactionId(transactionId);
    }

    public List<Payment> getPaymentsByBooking(String bookingId) {
        log.info("Fetching payments for booking ID: {}", bookingId);
        return paymentRepository.findByBookingId(bookingId);
    }

    public List<Payment> getPaymentsByUser(String userId) {
        log.info("Fetching payments for user ID: {}", userId);
        return paymentRepository.findByUserId(userId);
    }

    public Payment createPayment(Payment payment) {
        log.info("Creating new payment for booking: {}", payment.getBookingId());

        // Generate transaction ID
        payment.setTransactionId(generateTransactionId());
        payment.setStatus(Payment.PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }

    public Payment processPayment(String paymentId) {
        log.info("Processing payment with ID: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        payment.setStatus(Payment.PaymentStatus.PROCESSING);
        paymentRepository.save(payment);

        // Here you would integrate with actual payment gateway (Stripe, PayPal, etc.)
        // For now, we'll simulate a successful payment
        try {
            // Simulate payment processing
            Thread.sleep(1000);
            
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setProcessedAt(LocalDateTime.now());
            
            log.info("Payment processed successfully: {}", payment.getTransactionId());
        } catch (Exception e) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
            log.error("Payment processing failed: {}", e.getMessage());
        }

        return paymentRepository.save(payment);
    }

    public Payment refundPayment(String paymentId, BigDecimal refundAmount) {
        log.info("Refunding payment with ID: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setRefundedAmount(refundAmount);
        payment.setRefundedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public BigDecimal getTotalPaidAmount(String bookingId) {
        List<Payment> payments = paymentRepository.findByBookingId(bookingId);
        return payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalRevenueSince(LocalDateTime startDate) {
        List<Payment> allPayments = paymentRepository.findAll();
        return allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
                .filter(p -> p.getProcessedAt() != null && p.getProcessedAt().isAfter(startDate))
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 18).toUpperCase().replace("-", "");
    }

    public boolean isBookingFullyPaid(Booking booking) {
        BigDecimal totalPaid = getTotalPaidAmount(booking.getId());
        return totalPaid.compareTo(booking.getFinalAmount()) >= 0;
    }
}
