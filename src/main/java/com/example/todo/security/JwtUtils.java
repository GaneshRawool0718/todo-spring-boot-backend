package com.example.todo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    /*
     * Utility class for JWT operations such as generating, extracting, and validating JWT tokens.
     * It uses the secret key and expiration time configured in application properties.
     * This class integrates with Spring Security to manage authentication tokens.
     */
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // Default to 1 day if not set
    private long expiration;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        /*
         * Generates a JWT token for the given user details.
         * The token includes the username, issued at time, and expiration time.
         * It is signed with the configured secret key.
         */
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        /*
         * Extracts the username from the JWT token.
         * It parses the token and retrieves the subject (username).
         */
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        /*
         * Validates the JWT token against the user details.
         * It checks if the token is not expired and matches the username.
         */
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        /*
         * Checks if the JWT token is expired.
         * It parses the token and compares the expiration date with the current date.
         */
        Date expirationDate = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}
