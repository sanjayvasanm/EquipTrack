package com.equiptrack.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equiptrack.model.Booking;
import com.equiptrack.model.Equipment;
import com.equiptrack.model.User;
import com.equiptrack.repository.BookingRepository;
import com.equiptrack.repository.EquipmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing booking operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EquipmentRepository equipmentRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(String id) {
        log.info("Fetching booking with ID: {}", id);
        return bookingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Booking> getBookingByNumber(String bookingNumber) {
        log.info("Fetching booking with number: {}", bookingNumber);
        return bookingRepository.findByBookingNumber(bookingNumber);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByCustomer(String customerId) {
        log.info("Fetching bookings for customer ID: {}", customerId);
        return bookingRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByEquipment(String equipmentId) {
        log.info("Fetching bookings for equipment ID: {}", equipmentId);
        return bookingRepository.findByEquipmentId(equipmentId);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        log.info("Fetching bookings with status: {}", status);
        return bookingRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Booking> getRecentBookingsByCustomer(String customerId) {
        log.info("Fetching recent bookings for customer ID: {}", customerId);
        return bookingRepository.findRecentBookingsByCustomer(customerId);
    }

    @Transactional
    public Booking createBooking(Booking booking, User customer) {
        log.info("Creating new booking for customer: {}", customer.getEmail());

        // Validate equipment ID
        if (booking.getEquipmentId() == null || booking.getEquipmentId().isEmpty()) {
            throw new RuntimeException("Equipment ID is required");
        }

        // Fetch the full equipment entity to validate and get details
        Equipment equipment = equipmentRepository.findById(booking.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        log.info("Booking equipment: {} by customer: {}", equipment.getName(), customer.getEmail());

        // Set the customer ID
        booking.setCustomerId(customer.getId());

        // Validate equipment availability
        if (!isEquipmentAvailableForDates(booking.getEquipmentId(), 
                                         booking.getStartDate(), 
                                         booking.getEndDate())) {
            throw new RuntimeException("Equipment is not available for the selected dates");
        }

        // Generate booking number
        booking.setBookingNumber(generateBookingNumber());

        // Calculate total amount
        BigDecimal totalAmount = calculateTotalAmount(booking);
        booking.setTotalAmount(totalAmount);
        booking.setFinalAmount(totalAmount);

        // Set initial status
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.UNPAID);

        Booking savedBooking = bookingRepository.save(booking);

        // Update equipment status to RENTED
        equipment.setStatus(Equipment.EquipmentStatus.RENTED);
        equipmentRepository.save(equipment);

        // Send notification
        notificationService.sendBookingConfirmationNotification(savedBooking);

        log.info("Booking created successfully with number: {}", savedBooking.getBookingNumber());
        return savedBooking;
    }

    @Transactional
    public Booking confirmBooking(String id, User confirmedBy) {
        log.info("Confirming booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        // Mark payment as received for manual UPI flow
        booking.setPaymentStatus(Booking.PaymentStatus.PAID);
        booking.setConfirmedAt(LocalDateTime.now());
        booking.setConfirmedById(confirmedBy.getId());

        Booking savedBooking = bookingRepository.save(booking);
        notificationService.sendBookingStatusUpdateNotification(savedBooking);

        return savedBooking;
    }

    @Transactional
    public Booking startBooking(String id) {
        log.info("Starting booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        booking.setStatus(Booking.BookingStatus.IN_PROGRESS);
        booking.setActualPickupTime(LocalDateTime.now());

        Equipment equipment = equipmentRepository.findById(booking.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setStatus(Equipment.EquipmentStatus.RENTED);
        equipmentRepository.save(equipment);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking completeBooking(String id) {
        log.info("Completing booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setActualReturnTime(LocalDateTime.now());

        Equipment equipment = equipmentRepository.findById(booking.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setStatus(Equipment.EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment);

        Booking savedBooking = bookingRepository.save(booking);
        notificationService.sendBookingCompletedNotification(savedBooking);

        return savedBooking;
    }

    @Transactional
    public Booking cancelBooking(String id, String reason, User cancelledBy) {
        log.info("Cancelling booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking.setCancelledById(cancelledBy.getId());

        Equipment equipment = equipmentRepository.findById(booking.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        if (equipment.getStatus() == Equipment.EquipmentStatus.RESERVED ||
            equipment.getStatus() == Equipment.EquipmentStatus.RENTED) {
            equipment.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            equipmentRepository.save(equipment);
        }

        Booking savedBooking = bookingRepository.save(booking);
        notificationService.sendBookingCancelledNotification(savedBooking);

        return savedBooking;
    }

    @Transactional(readOnly = true)
    public boolean isEquipmentAvailableForDates(String equipmentId, LocalDate startDate, LocalDate endDate) {
        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                equipmentId, startDate, endDate);
        return conflictingBookings.isEmpty();
    }

    public BigDecimal calculateTotalAmount(Booking booking) {
        Equipment equipment = equipmentRepository.findById(booking.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate()) + 1;

        BigDecimal amount;
        if (days >= 30 && equipment.getMonthlyRate() != null) {
            long months = days / 30;
            long remainingDays = days % 30;
            amount = equipment.getMonthlyRate().multiply(BigDecimal.valueOf(months))
                    .add(equipment.getDailyRate().multiply(BigDecimal.valueOf(remainingDays)));
        } else if (days >= 7 && equipment.getWeeklyRate() != null) {
            long weeks = days / 7;
            long remainingDays = days % 7;
            amount = equipment.getWeeklyRate().multiply(BigDecimal.valueOf(weeks))
                    .add(equipment.getDailyRate().multiply(BigDecimal.valueOf(remainingDays)));
        } else {
            amount = equipment.getDailyRate().multiply(BigDecimal.valueOf(days));
        }

        // Add delivery fee if required
        if (booking.getRequiresDelivery() && booking.getDeliveryFee() != null) {
            amount = amount.add(booking.getDeliveryFee());
        }

        return amount;
    }

    private String generateBookingNumber() {
        long count = bookingRepository.count() + 1;
        String timestamp = String.valueOf(System.currentTimeMillis() % 100000);
        return String.format("BK%s%05d", timestamp, count);
    }

    public Long getBookingCountByStatus(Booking.BookingStatus status) {
        return bookingRepository.countByStatus(status);
    }

    public List<Booking> getBookingsEndingToday() {
        return bookingRepository.findBookingsEndingOn(LocalDate.now());
    }

    public List<Booking> getBookingsStartingToday() {
        return bookingRepository.findBookingsStartingOn(LocalDate.now());
    }

    /**
     * Computes the next available date for an equipment based on active (non-cancelled / non-completed)
     * bookings. If currently rented/reserved, availability is endDate + 1 of the latest active booking
     * overlapping today. Otherwise returns today.
     */
    public java.time.LocalDate getNextAvailableDateForEquipment(String equipmentId) {
        List<Booking> bookings = bookingRepository.findByEquipmentId(equipmentId);
        LocalDate today = LocalDate.now();
        LocalDate candidate = today;
        for (Booking b : bookings) {
            if (b.getStatus() == Booking.BookingStatus.CANCELLED || b.getStatus() == Booking.BookingStatus.COMPLETED) {
                continue;
            }
            // If currently within an active booking window
            if ((b.getStartDate().isBefore(today) || b.getStartDate().isEqual(today)) &&
                (b.getEndDate().isAfter(today) || b.getEndDate().isEqual(today))) {
                LocalDate next = b.getEndDate().plusDays(1);
                if (next.isAfter(candidate)) {
                    candidate = next;
                }
            }
        }
        return candidate;
    }
}
