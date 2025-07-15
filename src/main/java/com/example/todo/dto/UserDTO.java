package com.example.todo.dto;

import com.example.todo.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    // This class represents the data transfer object for user information.
    // It includes the user's ID, name, email, and role.
    private Integer id;
    private String name;
    private String email;
    private Role role;
}

