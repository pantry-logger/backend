package com.pantrylogger.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExternalAuthId(
        @NotNull
        @NotBlank
        String value) {
}
