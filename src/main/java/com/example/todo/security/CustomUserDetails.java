package com.example.todo.security;


import com.example.todo.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    /*
     * Custom implementation of UserDetails to expose additional user information
     * such as user ID and role.
     * This class wraps the User entity and provides methods required by Spring Security.
     * It implements UserDetails to integrate with Spring Security's authentication system.
     */

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return user.getId(); // Expose ID of user
    }

    public String getRole() {
        return user.getRole().name(); // Get role of user as a string
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + user.getRole().name()); // ROLE_USER or ROLE_ADMIN
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Assuming accounts are not expired by default, modify if you have an expiration field
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Assuming accounts are not locked by default, modify if you have a lock status field
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Assuming credentials are valid indefinitely, modify if you have a timestamp field
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Assuming users are enabled by default, modify if you have a status field
        return true;
    }
}
