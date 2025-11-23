package com.equiptrack.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByStripePaymentIntentId(String paymentIntentId);
    
    List<Payment> findByBookingId(String bookingId);
    
    List<Payment> findByUserId(String userId);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    @Query("{ 'bookingId': ?0, 'status': 'COMPLETED' }")
    List<Payment> findCompletedPaymentsByBooking(String bookingId);
    
    @Query(value = "{ 'bookingId': ?0, 'status': 'COMPLETED' }", fields = "{ 'amount': 1 }")
    List<Payment> findPaymentsForSum(String bookingId);
    
    @Query(value = "{ 'status': 'COMPLETED', 'createdAt': { $gte: ?0 } }", fields = "{ 'amount': 1 }")
    List<Payment> findPaymentsForRevenue(LocalDateTime startDate);
    
    @Query(value = "{ 'status': ?0 }", count = true)
    Long countByStatus(Payment.PaymentStatus status);
}
