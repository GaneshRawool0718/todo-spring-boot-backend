package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.todo.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Find user by email
    // This method retrieves a user based on their email address.
    Optional<User> findByEmail(String email);
}



