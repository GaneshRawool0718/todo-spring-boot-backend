package com.example.todo.config;

import com.example.todo.security.JwtAuthFilter;
// import com.example.todo.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
// import org.springframework.security.config.Customizer;


import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {   

    private final JwtAuthFilter jwtAuthFilter;
    // private final CustomUserDetailsService userDetailsService;

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Configure HttpSecurity to disable CSRF, enable CORS, and set up authorization rules
    // for different endpoints, including JWT authentication filter.    
    // Note: The CustomUserDetailsService is commented out as it is not used in this example.
    // If you need to use it, uncomment the relevant lines and configure it accordingly.
    return http
        .csrf(csrf -> csrf.disable()) // Disable CSRF protection
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))  //allow CORS (see cors bean below)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/signup", "/auth/login", "/forgot/send-otp",
                "/forgot/verify-otp",
                "/forgot/reset-password").permitAll() // open routes
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session management
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
        .build();
}


    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        // Create an AuthenticationManager bean using the provided AuthenticationConfiguration
        // This allows Spring Security to manage authentication processes.
        return config.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Create a PasswordEncoder bean using BCryptPasswordEncoder
        // This encoder will be used to hash passwords securely.
        return new BCryptPasswordEncoder();
    }

    @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    // Configure CORS settings to allow requests from specific origins, methods, and headers.
    // The allowed origin is set to "http://localhost:3000" for development purposes
    
    // Use addAllowedOriginPattern instead of setAllowedOrigins for flexibility
    config.addAllowedOriginPattern("http://localhost:3000");

    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
}

}
