package com.example.todo.controller;

import com.example.todo.dto.*;
import com.example.todo.model.Role;
import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final com.example.todo.security.CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
public ResponseEntity<?> register(@RequestBody UserCreateDTO dto) {
    System.out.println("Signup request: " + dto.getEmail());

    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        System.out.println("Email already exists: " + dto.getEmail());
        return ResponseEntity.badRequest().body("Email already registered.");
    }

    User user = new User();
    user.setEmail(dto.getEmail());
    user.setName(dto.getName());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole(Role.USER);

    userRepository.save(user);
    System.out.println("Registered user: " + user.getEmail());

    return ResponseEntity.ok("User registered successfully");
}


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByEmail(dto.getEmail()).get();

        AuthResponse response = new AuthResponse(token, user.getId(), user.getRole().name());
        return ResponseEntity.ok(response);
    }
}
