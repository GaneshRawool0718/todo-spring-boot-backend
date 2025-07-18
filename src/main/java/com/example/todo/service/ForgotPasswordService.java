// src/main/java/com/example/todo/service/ForgotPasswordService.java
package com.example.todo.service;

import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.model.OtpData;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {
    /*
     * Service for handling forgot password functionality.
     * It allows users to request an OTP, verify it, and reset their password.  
     * It uses an in-memory storage for OTPs, which is suitable for demonstration purposes.
     * In a production application, consider using a more persistent storage solution.
     */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final Map<String, OtpData> otpStorage = new HashMap<>();
    private static final int OTP_EXPIRY_MINUTES = 5; // OTP validity duration
    private static final int MAX_RESEND_ATTEMPTS = 3;  // Maximum number of times an OTP can be resent

    public boolean sendOtp(String email) {
        // Check if the user exists
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return false;

        OtpData existing = otpStorage.get(email);
        if (existing != null && existing.getResendCount() >= MAX_RESEND_ATTEMPTS) return false;

        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // Generate a 6-digit OTP
        // Create a new OTP data object with the generated OTP, expiry time, and resend count
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES); // Set expiry time to 5 minutes from now
        // If an existing OTP exists, increment the resend count; otherwise, start from 1
        int resendCount = (existing == null) ? 1 : existing.getResendCount() + 1; // Increment resend count

        otpStorage.put(email, new OtpData(otp, expiry, false, resendCount)); // Store the OTP data in memory

        sendEmail(email, otp); // Send the OTP to the user's email
        System.out.println("OTP for " + email + ": " + otp + " (Valid 5 mins)");
        return true;
    }

     private void sendEmail(String to, String otp) {
        // This method sends an email with the OTP to the user.
        // Ensure that the mailSender is properly configured in your application.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + "\nIt will expire in 5 minutes.");
        mailSender.send(message);
    }


    public boolean verifyOtp(String email, String otp) {
        // Verify the OTP for the given email
        OtpData otpData = otpStorage.get(email); // Retrieve OTP data from storage
        if (otpData == null) return false;
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) return false; // Check if OTP is expired
        if (otpData.isVerified()) return false; // Check if OTP is already verified
        if (!otpData.getOtp().equals(otp)) return false; // Check if the OTP matches

        otpData.setVerified(true);
        return true;
    }

    public boolean resetPassword(String email, String newPassword) {
        // Reset the password for the user after verifying the OTP
        OtpData otpData = otpStorage.get(email);
        if (otpData == null || !otpData.isVerified()) return false; // Check if OTP is valid and verified

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get(); // Get the user from the repository
        user.setPassword(passwordEncoder.encode(newPassword)); // Encode the new password
        userRepository.save(user); // Save the updated user

        otpStorage.remove(email); // Clear OTP
        return true;
    }
}
