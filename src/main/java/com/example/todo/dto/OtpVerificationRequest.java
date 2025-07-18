package com.example.todo.dto;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    // This class represents the data transfer object for OTP verification requests.
    // It includes the user's email and the OTP value for verification.
    private String email;
    private String otp;
}

