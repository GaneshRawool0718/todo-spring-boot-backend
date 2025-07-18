package com.example.todo.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    // This class represents the data transfer object for password reset requests.
    // It includes the user's email, new password, and confirmation of the new password.
    private String email;
    private String newPassword;
    private String confirmPassword;
}
