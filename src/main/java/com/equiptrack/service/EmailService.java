package com.equiptrack.service;

import com.equiptrack.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("EquipTrack - Verify Your Email");
            message.setText("Welcome to EquipTrack!\n\n" +
                    "Please click the link below to verify your email address:\n" +
                    "http://localhost:8080/verify-email?token=" + user.getVerificationToken() +
                    "\n\nThank you for choosing EquipTrack!");

            mailSender.send(message);
            log.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error sending verification email to: {}", user.getEmail(), e);
        }
    }

    @Async
    public void sendPasswordResetEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("EquipTrack - Password Reset");
            message.setText("You have requested to reset your password.\n\n" +
                    "Please click the link below to reset your password:\n" +
                    "http://localhost:8080/reset-password?token=" + user.getResetPasswordToken() +
                    "\n\nThis link will expire in 24 hours.\n\n" +
                    "If you did not request this, please ignore this email.");

            mailSender.send(message);
            log.info("Password reset email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error sending password reset email to: {}", user.getEmail(), e);
        }
    }

    @Async
    public void sendBookingConfirmationEmail(String toEmail, String bookingNumber, String equipmentName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("EquipTrack - Booking Confirmation #" + bookingNumber);
            message.setText("Your booking has been confirmed!\n\n" +
                    "Booking Number: " + bookingNumber + "\n" +
                    "Equipment: " + equipmentName + "\n\n" +
                    "You can view your booking details at:\n" +
                    "http://localhost:8080/my-bookings\n\n" +
                    "Thank you for choosing EquipTrack!");

            mailSender.send(message);
            log.info("Booking confirmation email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending booking confirmation email to: {}", toEmail, e);
        }
    }

    @Async
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email sent to: {}", to);
        } catch (Exception e) {
            log.error("Error sending email to: {}", to, e);
        }
    }
}
