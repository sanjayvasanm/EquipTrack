package com.equiptrack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equiptrack.model.Booking;
import com.equiptrack.model.Notification;
import com.equiptrack.model.User;
import com.equiptrack.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing notifications
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public List<Notification> getNotificationsByUser(String userId) {
        log.info("Fetching notifications for user ID: {}", userId);
        return notificationRepository.findRecentNotificationsByUser(userId);
    }

    public List<Notification> getUnreadNotificationsByUser(String userId) {
        log.info("Fetching unread notifications for user ID: {}", userId);
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public Long getUnreadCount(String userId) {
        return notificationRepository.countUnreadByUser(userId);
    }

    public Notification createNotification(User user, Notification.NotificationType type, 
                                          String title, String message, String link) {
        log.info("Creating notification for user: {}", user.getEmail());

        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setLink(link);
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }

    public void markAsRead(String notificationId) {
        log.info("Marking notification {} as read", notificationId);

        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead(String userId) {
        log.info("Marking all notifications as read for user: {}", userId);

        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        });
        notificationRepository.saveAll(notifications);
    }

    public void sendBookingConfirmationNotification(Booking booking) {
        // Fetch customer by ID
        User customer = userService.getUserById(booking.getCustomerId())
                .orElse(null);
        
        if (customer == null) {
            log.warn("Cannot send notification - customer not found for booking: {}", booking.getId());
            return;
        }
        
        createNotification(
                customer,
                Notification.NotificationType.BOOKING_CONFIRMED,
                "Booking Confirmed",
                "Your booking #" + booking.getBookingNumber() + " has been created successfully.",
                "/my-bookings/" + booking.getId()
        );
    }

    public void sendBookingStatusUpdateNotification(Booking booking) {
        User customer = userService.getUserById(booking.getCustomerId())
                .orElse(null);
        
        if (customer == null) {
            log.warn("Cannot send notification - customer not found for booking: {}", booking.getId());
            return;
        }
        
        createNotification(
                customer,
                Notification.NotificationType.BOOKING_CONFIRMED,
                "Booking Status Updated",
                "Your booking #" + booking.getBookingNumber() + " status has been updated to " + booking.getStatus(),
                "/my-bookings/" + booking.getId()
        );
    }

    public void sendBookingCancelledNotification(Booking booking) {
        User customer = userService.getUserById(booking.getCustomerId())
                .orElse(null);
        
        if (customer == null) {
            log.warn("Cannot send notification - customer not found for booking: {}", booking.getId());
            return;
        }
        
        createNotification(
                customer,
                Notification.NotificationType.BOOKING_CANCELLED,
                "Booking Cancelled",
                "Your booking #" + booking.getBookingNumber() + " has been cancelled.",
                "/my-bookings/" + booking.getId()
        );
    }

    public void sendBookingCompletedNotification(Booking booking) {
        User customer = userService.getUserById(booking.getCustomerId())
                .orElse(null);
        
        if (customer == null) {
            log.warn("Cannot send notification - customer not found for booking: {}", booking.getId());
            return;
        }
        
        createNotification(
                customer,
                Notification.NotificationType.BOOKING_CONFIRMED,
                "Booking Completed",
                "Your booking #" + booking.getBookingNumber() + " has been completed. Thank you!",
                "/my-bookings/" + booking.getId()
        );
    }

    public void sendPaymentReceivedNotification(User user, String bookingNumber, String amount) {
        createNotification(
                user,
                Notification.NotificationType.PAYMENT_RECEIVED,
                "Payment Received",
                "Payment of $" + amount + " received for booking #" + bookingNumber,
                "/my-bookings"
        );
    }

    public void deleteNotification(String id) {
        log.info("Deleting notification with ID: {}", id);
        notificationRepository.deleteById(id);
    }
}
