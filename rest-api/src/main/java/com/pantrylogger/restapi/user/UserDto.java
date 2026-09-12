package com.pantrylogger.restapi.user;

import java.util.UUID;

import com.pantrylogger.domain.user.User;

public record UserDto(
        UUID uuid,
        String email,
        String username,
        String firstName,
        String lastName
) {
    public UserDto(User user) {
        this(
                user.getUuid().uuid(),
                user.getEmail().toString(),
                user.getUsername().toString(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
