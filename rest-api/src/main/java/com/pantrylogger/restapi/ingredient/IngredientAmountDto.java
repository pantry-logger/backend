package com.pantrylogger.restapi.ingredient;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;

public record IngredientAmountDto(
        IngredientDto ingredient,
        int amount,
        IngredientAmountUnit unit) {
    public IngredientAmountDto(IngredientAmount ingredientAmount) {
        this(
                new IngredientDto(ingredientAmount.getIngredient()),
                ingredientAmount.getAmount(),
                ingredientAmount.getUnit()
        );

    }
}