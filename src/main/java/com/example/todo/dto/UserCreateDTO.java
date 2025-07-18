package com.example.todo.dto;


import lombok.Data;

@Data
public class UserCreateDTO {
    // This class represents the data transfer object for creating a new user.
    // It includes the user's name, email, and password.
    private String name;
    private String email;
    private String password;
}

