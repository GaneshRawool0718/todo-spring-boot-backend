package com.example.todo.model;

// src/main/java/com/example/todo/model/OtpData.java
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpData {
    private String otp;
    private LocalDateTime expiryTime;
    private boolean verified;
    private int resendCount;
}
