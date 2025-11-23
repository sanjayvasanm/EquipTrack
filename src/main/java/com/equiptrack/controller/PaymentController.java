package com.equiptrack.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equiptrack.model.Booking;
import com.equiptrack.model.Payment;
import com.equiptrack.model.User;
import com.equiptrack.service.BookingService;
import com.equiptrack.service.PaymentService;
import com.equiptrack.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for payment operations (simplified manual flow).
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final UserService userService;

    /**
     * Create a manual (offline/UPI) payment for a booking. Marks payment as COMPLETED.
     * If booking was PENDING it will be CONFIRMED and paymentStatus set to PAID.
     */
    @PostMapping("/manual")
    public ResponseEntity<?> createManualPayment(@RequestParam String bookingId,
                                                 @AuthenticationPrincipal UserDetails principal) {
        Booking booking = bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (principal == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }
        User user = userService.getUserByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Build payment
        Payment payment = new Payment();
        payment.setBookingId(booking.getId());
        payment.setUserId(user.getId());
        BigDecimal amount = booking.getFinalAmount() != null ? booking.getFinalAmount() : booking.getTotalAmount();
        payment.setAmount(amount);
        payment.setPaymentMethod(Payment.PaymentMethod.OTHER); // Representing UPI/manual
        payment.setType(Payment.PaymentType.BOOKING_PAYMENT);
        payment.setDescription("Manual UPI payment for booking " + booking.getBookingNumber());

        // Persist initial payment (creates transactionId + PENDING)
        payment = paymentService.createPayment(payment);
        // Mark as completed immediately (manual trust flow)
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setProcessedAt(LocalDateTime.now());
        paymentService.processPayment(payment.getId()); // simulate processing -> sets COMPLETED

        // Update booking payment status and confirm booking
        if (booking.getStatus() == Booking.BookingStatus.PENDING) {
            // Use bookingService.confirmBooking to properly update and save
            bookingService.confirmBooking(booking.getId(), user);
        } else {
            // Just update payment status for non-pending bookings
            booking.setPaymentStatus(Booking.PaymentStatus.PAID);
        }

        log.info("Manual payment completed for booking {}", booking.getBookingNumber());
        return ResponseEntity.ok(payment);
    }

    /**
     * List payments for a booking.
     */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> listPaymentsForBooking(@PathVariable String bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBooking(bookingId);
        return ResponseEntity.ok(payments);
    }
}
