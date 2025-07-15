package com.example.todo.controller;


import com.example.todo.model.User;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    // Get logged-in user's profile
    @GetMapping("/profile")
    public User getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Get all tasks for logged-in user
    @GetMapping("/tasks")
    public List<Task> getUserTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return taskRepository.findByUserId(userDetails.getUserId());
    }
}

