package com.pantrylogger.domain.recipe.ingredient.add;

import com.pantrylogger.domain.ingredient.IngredientAmountUnit;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddIngredientAmountCommand(
    @NotNull
    IngredientUUID ingredientUUID,

    @Min(value = 1, message = "Ingredient Amount must be greater than 0")
    int amount,

    @NotNull
    IngredientAmountUnit unit
) {
}