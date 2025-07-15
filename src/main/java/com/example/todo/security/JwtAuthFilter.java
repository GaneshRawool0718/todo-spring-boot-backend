package com.example.todo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    /*
     * JWT Authentication Filter to intercept requests and validate JWT tokens.
     * This filter checks the Authorization header for a valid JWT token,
     * extracts user details, and sets the authentication in the security context.
     * It skips authentication for public paths like /auth.
     */

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {

    String path = request.getServletPath();
    System.out.println("Request path: " + path);

    if (path.startsWith("/auth")) {
        System.out.println("Skipping JWT filter for public path: " + path);
        filterChain.doFilter(request, response);
        return;
    }

    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        System.out.println("Missing or invalid Authorization header");
        filterChain.doFilter(request, response);
        return;
    }

    final String jwt = authHeader.substring(7);
    final String userEmail = jwtUtils.extractUsername(jwt);
    System.out.println("Extracted email: " + userEmail);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        // Load user details using the custom UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (jwtUtils.validateToken(jwt, userDetails)) {
            // Create authentication token and set it in the security context
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("Authenticated user: " + userEmail);
        } else {
            System.out.println("Invalid JWT token for user: " + userEmail);
        }
    }

    filterChain.doFilter(request, response); // Continue the filter chain
}
}
