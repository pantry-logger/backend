package com.pantrylogger.restapi.security;

import java.util.Set;

import jakarta.annotation.Nonnull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pantrylogger.domain.user.User;

public class CustomUserDetails implements UserDetails {

    // UserDetails extends Serializable for session-replication support,
    // but this app is stateless (JWT auth) — no session ever serializes
    // this object. Keeping `user` transient avoids forcing the domain
    // User class to implement Serializable just to satisfy this contract.
    private final transient User user;
    private final Set<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
            User user,
            Set<? extends GrantedAuthority> authorities
    ) {
        this.user = user;
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    @Nonnull
    public String getUsername() {
        return user.getUsername().username();
    }
}
