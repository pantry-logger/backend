package com.pantrylogger.domain.user;

import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.annotation.Nonnull;

import com.pantrylogger.domain.exception.InvalidEmailException;

public record Email(String address) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (address == null || address.isBlank()) {
            throw new InvalidEmailException(
                    "Email cannot be null or blank");
        }
        address = address.trim().toLowerCase(Locale.ENGLISH);
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new InvalidEmailException("Invalid email: " + address);
        }
    }

    @Override
    @Nonnull
    public String toString() {
        return address;
    }
}
