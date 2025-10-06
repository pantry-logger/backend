package com.pantrylogger.domain.recipe.ingredient.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.pantrylogger.domain.ingredient.IngredientAmountUnit;

public record UpdateIngredientAmountCommand(

        @Min(value = 1, message = "Ingredient Amount must be greater than 0")
        int amount,

        @NotNull
        IngredientAmountUnit unit) {
}