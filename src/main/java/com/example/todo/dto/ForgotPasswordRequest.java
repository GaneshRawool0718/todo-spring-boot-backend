package com.example.todo.dto;



import lombok.Data;

@Data
public class ForgotPasswordRequest {
    // This class represents the data transfer object for forgot password requests.
    // It includes the user's email for which the password reset is requested.
    private String email;
}

