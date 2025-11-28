package com.equiptrack.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equiptrack.model.Booking;
import com.equiptrack.model.Equipment;
import com.equiptrack.model.Location;
import com.equiptrack.model.User;
import com.equiptrack.service.BookingService;
import com.equiptrack.service.EquipmentService;
import com.equiptrack.service.LocationService;
import com.equiptrack.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * REST API controller for booking operations
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingApiController {

    private final BookingService bookingService;
    private final EquipmentService equipmentService;
    private final UserService userService;
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrichedBookingResponse> getBookingById(@PathVariable String id) {
        return bookingService.getBookingById(id)
                .map(booking -> {
                    Equipment equipment = equipmentService.getEquipmentById(booking.getEquipmentId())
                            .orElse(null);
                    Location location = equipment != null && equipment.getLocationId() != null 
                        ? locationService.getLocationById(equipment.getLocationId()).orElse(null) 
                        : null;
                    return ResponseEntity.ok(new EnrichedBookingResponse(booking, equipment, location));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getBookingsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<EnrichedBookingResponse>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Booking> bookings = bookingService.getRecentBookingsByCustomer(user.getId());
        
        // Enrich bookings with equipment and location data
        List<EnrichedBookingResponse> enrichedBookings = bookings.stream()
                .map(booking -> {
                    Equipment equipment = equipmentService.getEquipmentById(booking.getEquipmentId())
                            .orElse(null);
                    Location location = equipment != null && equipment.getLocationId() != null
                        ? locationService.getLocationById(equipment.getLocationId()).orElse(null)
                        : null;
                    return new EnrichedBookingResponse(booking, equipment, location);
                })
                .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.ok(enrichedBookings);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest bookingRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Fetch actual User entity from email
            User user = userService.getUserByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Convert BookingRequest to Booking
            Booking booking = bookingRequest.toBooking();
            
            Booking createdBooking = bookingService.createBooking(booking, user);
            
            // Convert to DTO to avoid lazy loading issues
            BookingResponse response = new BookingResponse(createdBooking);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * DTO for booking creation requests
     */
    @lombok.Data
    public static class BookingRequest {
        private String equipmentId;
        private String startDate;
        private String endDate;
        private String customerNotes;
        
        public Booking toBooking() {
            Booking booking = new Booking();
            
            // Set equipment ID directly (MongoDB uses String IDs)
            booking.setEquipmentId(equipmentId);
            
            // Set dates
            booking.setStartDate(java.time.LocalDate.parse(startDate));
            booking.setEndDate(java.time.LocalDate.parse(endDate));
            
            // Set notes
            booking.setCustomerNotes(customerNotes);
            
            return booking;
        }
    }
    
    /**
     * DTO for booking response - prevents lazy loading issues
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class BookingResponse {
        private String id;
        private String bookingNumber;
        private String status;
        private java.math.BigDecimal totalAmount;
        private java.math.BigDecimal finalAmount;
        private String startDate;
        private String endDate;
        private String customerNotes;
        
        public BookingResponse(Booking booking) {
            this.id = booking.getId();
            this.bookingNumber = booking.getBookingNumber();
            this.status = booking.getStatus() != null ? booking.getStatus().toString() : null;
            this.totalAmount = booking.getTotalAmount();
            this.finalAmount = booking.getFinalAmount();
            this.startDate = booking.getStartDate() != null ? booking.getStartDate().toString() : null;
            this.endDate = booking.getEndDate() != null ? booking.getEndDate().toString() : null;
            this.customerNotes = booking.getCustomerNotes();
        }
    }
    
    /**
     * DTO for enriched booking response with equipment and location data
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class EnrichedBookingResponse {
        private String id;
        private String bookingNumber;
        private String status;
        private String paymentStatus;
        private java.math.BigDecimal totalAmount;
        private java.math.BigDecimal finalAmount;
        private String startDate;
        private String endDate;
        private String customerNotes;
        private EquipmentDTO equipment;
        private LocationDTO location;
        
        public EnrichedBookingResponse(Booking booking, Equipment equipment, Location location) {
            this.id = booking.getId();
            this.bookingNumber = booking.getBookingNumber();
            this.status = booking.getStatus() != null ? booking.getStatus().toString() : null;
            this.paymentStatus = booking.getPaymentStatus() != null ? booking.getPaymentStatus().toString() : null;
            this.totalAmount = booking.getTotalAmount();
            this.finalAmount = booking.getFinalAmount();
            this.startDate = booking.getStartDate() != null ? booking.getStartDate().toString() : null;
            this.endDate = booking.getEndDate() != null ? booking.getEndDate().toString() : null;
            this.customerNotes = booking.getCustomerNotes();
            
            if (equipment != null) {
                this.equipment = new EquipmentDTO(equipment);
            }
            
            if (location != null) {
                this.location = new LocationDTO(location);
            }
        }
    }
    
    /**
     * DTO for equipment data in booking response
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class EquipmentDTO {
        private String id;
        private String name;
        private String description;
        private java.math.BigDecimal dailyRate;
        private String imageUrl;
        
        public EquipmentDTO(Equipment equipment) {
            this.id = equipment.getId();
            this.name = equipment.getName();
            this.description = equipment.getDescription();
            this.dailyRate = equipment.getDailyRate();
            this.imageUrl = equipment.getImageUrl();
        }
    }

    /**
     * DTO for location/warehouse data in booking response
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class LocationDTO {
        private String id;
        private String name;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String phoneNumber;
        private String email;
        private String operatingHours;
        
        public LocationDTO(Location location) {
            this.id = location.getId();
            this.name = location.getName();
            this.address = location.getAddress();
            this.city = location.getCity();
            this.state = location.getState();
            this.zipCode = location.getZipCode();
            this.phoneNumber = location.getPhoneNumber();
            this.email = location.getEmail();
            this.operatingHours = location.getOperatingHours();
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(bookingService.confirmBooking(id, user));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Booking> approveBooking(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(bookingService.approveBooking(id, user));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Booking> startBooking(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.startBooking(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Booking> completeBooking(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable String id,
            @RequestParam String reason,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(bookingService.cancelBooking(id, reason, user));
    }
}
