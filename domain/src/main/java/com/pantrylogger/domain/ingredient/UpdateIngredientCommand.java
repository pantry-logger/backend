package com.pantrylogger.domain.ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateIngredientCommand(
        @NotNull
        @NotBlank
        @Size(min = 2, max = 50, message = "Ingredient Name must be between 2 and 50 characters")
        String name,

        @NotNull
        String description) {
}