package com.equiptrack.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.equiptrack.model.Booking;
import com.equiptrack.model.Category;
import com.equiptrack.model.Equipment;
import com.equiptrack.model.Location;
import com.equiptrack.model.User;
import com.equiptrack.service.BookingService;
import com.equiptrack.service.CategoryService;
import com.equiptrack.service.EquipmentService;
import com.equiptrack.service.LocationService;
import com.equiptrack.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Web controller for equipment-related pages
 */
@Controller
@RequiredArgsConstructor
public class WebController {

    private final EquipmentService equipmentService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Equipment> featuredEquipment = equipmentService.getFeaturedEquipment();
        model.addAttribute("featuredEquipment", featuredEquipment);
        model.addAttribute("currentUser", userDetails);
        return "index";
    }

    @GetMapping("/browse-equipment")
    public String browseEquipment(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String locationId,
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        
        List<Equipment> equipmentList;
        
        if (search != null && !search.isEmpty()) {
            equipmentList = equipmentService.searchEquipment(search);
        } else if (categoryId != null) {
            equipmentList = equipmentService.getAvailableEquipmentByCategory(categoryId);
        } else if (locationId != null) {
            equipmentList = equipmentService.getEquipmentByLocation(locationId);
        } else {
            equipmentList = equipmentService.getAvailableEquipment();
        }

        List<Category> categories = categoryService.getAllActiveCategories();
        List<Location> locations = locationService.getAllActiveLocations();

        // Build maps for category and location lookup
        java.util.Map<String, Category> categoryMap = new java.util.HashMap<>();
        for (Category cat : categories) {
            categoryMap.put(cat.getId(), cat);
        }
        java.util.Map<String, Location> locationMap = new java.util.HashMap<>();
        for (Location loc : locations) {
            locationMap.put(loc.getId(), loc);
        }

        // Build availability map (next available date if currently rented)
        java.util.Map<String, String> availabilityMap = new java.util.HashMap<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (Equipment eq : equipmentList) {
            if (eq.getStatus() == Equipment.EquipmentStatus.RENTED || eq.getStatus() == Equipment.EquipmentStatus.RESERVED) {
                java.time.LocalDate next = bookingService.getNextAvailableDateForEquipment(eq.getId());
                availabilityMap.put(eq.getId(), next.isAfter(today) ? next.toString() : "Soon");
            } else {
                availabilityMap.put(eq.getId(), "Now");
            }
        }
        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("availabilityMap", availabilityMap);
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("locationMap", locationMap);
        model.addAttribute("categories", categories);
        model.addAttribute("locations", locations);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedLocation", locationId);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentUser", userDetails);

        return "browse-equipment";
    }

    @GetMapping("/equipment/{id}")
    public String equipmentDetails(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Equipment equipment = equipmentService.getEquipmentById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        
        // Fetch category and location by ID
        Category category = categoryService.getCategoryById(equipment.getCategoryId())
                .orElse(null);
        Location location = locationService.getLocationById(equipment.getLocationId())
                .orElse(null);
        
        String availabilityMessage;
        if (equipment.getStatus() == Equipment.EquipmentStatus.RENTED || equipment.getStatus() == Equipment.EquipmentStatus.RESERVED) {
            java.time.LocalDate next = bookingService.getNextAvailableDateForEquipment(equipment.getId());
            availabilityMessage = "Available from: " + next.toString();
        } else {
            availabilityMessage = "Available now";
        }

        model.addAttribute("equipment", equipment);
        model.addAttribute("category", category);
        model.addAttribute("location", location);
        model.addAttribute("availabilityMessage", availabilityMessage);
        model.addAttribute("currentUser", userDetails);
        return "equipment-details";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/my-bookings")
    public String myBookings() {
        return "my-bookings";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/admin/dashboard")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {
        // Get all bookings
        List<Booking> allBookings = bookingService.getAllBookings();
        
        // Calculate statistics
        long totalBookings = allBookings.size();
        BigDecimal totalRevenue = allBookings.stream()
                .filter(b -> b.getPaymentStatus() == Booking.PaymentStatus.PAID)
                .map(Booking::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long activeRentals = allBookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.IN_PROGRESS)
                .count();
        long pendingBookings = allBookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.PENDING)
                .count();
        long completedBookings = allBookings.stream()
            .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
            .count();
        
        // Get recent bookings (last 20)
        List<Booking> recentBookings = allBookings.stream()
                .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(20)
                .toList();
        
        // Get all equipment + availability map
        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        java.util.Map<String, String> availabilityMap = new java.util.HashMap<>();
        java.util.Map<String, Long> equipmentBookingsCount = new java.util.HashMap<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (Equipment eq : equipmentList) {
            if (eq.getStatus() == Equipment.EquipmentStatus.RENTED || eq.getStatus() == Equipment.EquipmentStatus.RESERVED) {
                java.time.LocalDate next = bookingService.getNextAvailableDateForEquipment(eq.getId());
                availabilityMap.put(eq.getId(), next.isAfter(today) ? next.toString() : "Soon");
            } else {
                availabilityMap.put(eq.getId(), "Now");
            }
            // Count bookings for this equipment
            long eqBookings = allBookings.stream().filter(b -> b.getEquipmentId().equals(eq.getId())).count();
            equipmentBookingsCount.put(eq.getId(), eqBookings);
        }
        
        // Get all users
        List<User> users = userService.getAllUsers();
        Long customerCount = userService.getCustomerCount();
        java.util.Map<String, java.math.BigDecimal> userSpentMap = new java.util.HashMap<>();
        for (User u : users) {
            java.math.BigDecimal spent = allBookings.stream()
                    .filter(b -> b.getCustomerId().equals(u.getId()))
                    .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED || 
                                 b.getPaymentStatus() == Booking.PaymentStatus.PAID || 
                                 b.getPaymentStatus() == Booking.PaymentStatus.PARTIALLY_PAID)
                    .map(b -> b.getFinalAmount() != null ? b.getFinalAmount() : java.math.BigDecimal.ZERO)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            userSpentMap.put(u.getId(), spent);
        }

        // Build lookup maps for templates (equipment and customers)
        java.util.Map<String, Equipment> equipmentMap = new java.util.HashMap<>();
        for (Equipment eq : equipmentList) {
            equipmentMap.put(eq.getId(), eq);
        }

        java.util.Map<String, User> customerMap = new java.util.HashMap<>();
        for (User u : users) {
            customerMap.put(u.getId(), u);
        }
        
        // Build cancelledBy map for showing who cancelled bookings
        java.util.Map<String, String> cancelledByMap = new java.util.HashMap<>();
        for (Booking b : allBookings) {
            if (b.getCancelledById() != null) {
                User cancelledBy = userService.getUserById(b.getCancelledById()).orElse(null);
                if (cancelledBy != null) {
                    cancelledByMap.put(b.getId(), cancelledBy.getFullName() + " (" + cancelledBy.getRole() + ")");
                }
            }
        }
        
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("activeRentals", activeRentals);
        model.addAttribute("pendingBookings", pendingBookings);
        model.addAttribute("completedBookings", completedBookings);
        model.addAttribute("recentBookings", recentBookings);
        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("availabilityMap", availabilityMap);
        model.addAttribute("equipmentMap", equipmentMap);
        model.addAttribute("customerMap", customerMap);
        model.addAttribute("cancelledByMap", cancelledByMap);
        model.addAttribute("equipmentBookingsCount", equipmentBookingsCount);
        model.addAttribute("users", users);
        model.addAttribute("customerCount", customerCount);
        model.addAttribute("userSpentMap", userSpentMap);
        
        return "admin-dashboard";
    }

    @GetMapping("/admin/equipment")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public String showAdminEquipmentManagement(Model model) {
        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        List<Category> categories = categoryService.getAllActiveCategories();
        List<Location> locations = locationService.getAllActiveLocations();
        
        // Build lookup maps
        java.util.Map<String, Category> categoryMap = new java.util.HashMap<>();
        for (Category cat : categories) {
            categoryMap.put(cat.getId(), cat);
        }
        java.util.Map<String, Location> locationMap = new java.util.HashMap<>();
        for (Location loc : locations) {
            locationMap.put(loc.getId(), loc);
        }
        
        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("categories", categories);
        model.addAttribute("locations", locations);
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("locationMap", locationMap);
        
        return "admin-equipment";
    }

    @GetMapping("/payment")
    public String payment(@RequestParam String bookingId, Model model) {
        model.addAttribute("bookingId", bookingId);
        return "payment";
    }

    @GetMapping("/admin/booking-view/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public String viewBookingDetails(@PathVariable String id, Model model) {
        Booking booking = bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        Equipment equipment = equipmentService.getEquipmentById(booking.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        User customer = userService.getUserById(booking.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Category category = categoryService.getCategoryById(equipment.getCategoryId()).orElse(null);
        
        // Get location/warehouse information
        Location location = locationService.getLocationById(equipment.getLocationId()).orElse(null);
        
        // Calculate duration in days
        long duration = java.time.temporal.ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        
        // Get cancelledBy user if booking was cancelled
        String cancelledByName = null;
        if (booking.getCancelledById() != null) {
            User cancelledBy = userService.getUserById(booking.getCancelledById()).orElse(null);
            if (cancelledBy != null) {
                cancelledByName = cancelledBy.getFullName() + " (" + cancelledBy.getRole() + ")";
            }
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("equipment", equipment);
        model.addAttribute("customer", customer);
        model.addAttribute("category", category);
        model.addAttribute("location", location);
        model.addAttribute("duration", duration);
        model.addAttribute("cancelledByName", cancelledByName);
        
        return "booking-view";
    }

}
