package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.model.User;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    // AdminController handles admin-specific operations
    // It allows admins to view all users and their tasks
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        // This method retrieves all users in the system
        // It is accessible only to admins
        return userRepository.findAll();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile")
    public User getAdminProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // This method retrieves the profile of the currently authenticated admin user
        // It uses the UserDetails object to find the admin user by email
        return userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}/tasks")
    public List<Task> getTasksByUser(@PathVariable Integer id) {
        // This method retrieves all tasks for a specific user by their ID
        // It is accessible only to admins
        return taskRepository.findByUserId(id);
    }
}
