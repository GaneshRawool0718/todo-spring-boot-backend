package com.example.todo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    // This class represents the authentication response after a successful login or registration.
    // It includes the JWT token, user ID, and user role.
    private String token;
    private Integer id;
    private String role;
}

