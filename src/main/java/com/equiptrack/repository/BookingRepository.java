package com.equiptrack.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    
    Optional<Booking> findByBookingNumber(String bookingNumber);
    
    List<Booking> findByCustomerId(String customerId);
    
    List<Booking> findByEquipmentId(String equipmentId);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    List<Booking> findByCustomerIdAndStatus(String customerId, Booking.BookingStatus status);
    
    @Query("{ 'equipmentId': ?0, 'status': { $nin: ['CANCELLED', 'COMPLETED'] }, $and: [ { 'startDate': { $lte: ?2 } }, { 'endDate': { $gte: ?1 } } ] }")
    List<Booking> findConflictingBookings(String equipmentId, LocalDate startDate, LocalDate endDate);
    
    @Query("{ 'startDate': { $gte: ?0, $lte: ?1 } }")
    List<Booking> findBookingsByDateRange(LocalDate startDate, LocalDate endDate);
    
    @Query(value = "{ 'customerId': ?0 }", sort = "{ 'createdAt': -1 }")
    List<Booking> findRecentBookingsByCustomer(String customerId);
    
    @Query(value = "{ 'status': ?0 }", count = true)
    Long countByStatus(Booking.BookingStatus status);
    
    @Query(value = "{ 'createdAt': { $gte: ?0 } }", count = true)
    Long countBookingsSince(LocalDateTime startDate);
    
    @Query("{ 'endDate': ?0, 'status': 'IN_PROGRESS' }")
    List<Booking> findBookingsEndingOn(LocalDate date);
    
    @Query("{ 'startDate': ?0, 'status': 'CONFIRMED' }")
    List<Booking> findBookingsStartingOn(LocalDate date);
}
