package com.example.todo.controller;


import com.example.todo.dto.*;
import com.example.todo.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgot")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ForgotPasswordController {
    /*
     * ForgotPasswordController handles the forgot password functionality.
     * It allows users to request an OTP, verify it, and reset their password.  
     * It uses the ForgotPasswordService to perform the operations.
     */
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody ForgotPasswordRequest request) {
        // This method sends an OTP to the user's email for password reset.
        // It expects a ForgotPasswordRequest object containing the user's email.
        boolean success = forgotPasswordService.sendOtp(request.getEmail());
        return success ? ResponseEntity.ok("OTP sent") : ResponseEntity.badRequest().body("User not found");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        // This method verifies the OTP sent to the user's email.
        // It expects an OtpVerificationRequest object containing the user's email and the OTP value.
        boolean valid = forgotPasswordService.verifyOtp(request.getEmail(), request.getOtp());
        return valid ? ResponseEntity.ok("OTP verified") : ResponseEntity.badRequest().body("Invalid OTP");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        // This method resets the user's password.
        // It expects a PasswordResetRequest object containing the user's email, new password, and confirmation of the new password.
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        boolean reset = forgotPasswordService.resetPassword(request.getEmail(), request.getNewPassword());
        return reset ? ResponseEntity.ok("Password updated successfully") : ResponseEntity.badRequest().body("User not found");
    }
}
