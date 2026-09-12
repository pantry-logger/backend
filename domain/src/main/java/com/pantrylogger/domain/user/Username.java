package com.pantrylogger.domain.user;

import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.annotation.Nonnull;

import com.pantrylogger.domain.exception.InvalidUsernameException;

public record Username(String username) {

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+$");

    public Username {
        if (username == null || username.isBlank()) {
            throw new InvalidUsernameException(
                    "Username cannot be null or blank");
        }
        username = username.trim().toLowerCase(Locale.ENGLISH);
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new InvalidUsernameException("Invalid username: " + username);
        }
    }

    @Override
    @Nonnull
    public String toString() {
        return username;
    }
}
