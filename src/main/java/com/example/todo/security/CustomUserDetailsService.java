package com.example.todo.security;

import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    /*
     * Custom UserDetailsService to load user details by email.
     * This service uses the UserRepository to fetch user data and returns a CustomUserDetails object.
     * It integrates with Spring Security's authentication system.
     */

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user by email from the repository
        // If user not found, throw UsernameNotFoundException
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

    return new CustomUserDetails(user); // use your custom implementation
}

}
