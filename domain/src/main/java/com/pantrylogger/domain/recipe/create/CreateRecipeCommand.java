package com.pantrylogger.domain.recipe.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRecipeCommand(
        @NotNull
        @NotBlank 
        @Size(min = 5, max = 50, message = "Recipe Name must be between 5 and 50 characters") 
        String name,

        @NotNull String description) {
}