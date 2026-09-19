package com.pantrylogger.domain.shoppinglist.item.add;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;

public record AddItemToShoppingListCommand(
        @NotNull
        IngredientUUID ingredientUUID,
        @NotNull
        @Positive
        int amountValue,
        @NotNull
        IngredientAmountUnit ingredientAmountUnit
) {
}
