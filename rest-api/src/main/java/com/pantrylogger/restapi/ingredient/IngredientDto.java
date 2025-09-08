package com.pantrylogger.restapi.ingredient;

import java.util.UUID;

import com.pantrylogger.domain.ingredient.Ingredient;

public record IngredientDto(
        UUID uuid,
        String name,
        String description) {

    public IngredientDto(Ingredient ingredient) {
        this(
                ingredient.getUuid().uuid(),
                ingredient.getName(),
                ingredient.getDescription());
    }
}