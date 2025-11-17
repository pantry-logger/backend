package com.pantrylogger.restapi.ingredient;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;
import com.pantrylogger.domain.ingredient.amount.Amount;

public record IngredientAmountDto(
        IngredientDto ingredient,
        int amount,
        IngredientAmountUnit unit) {

    public IngredientAmountDto(IngredientAmount ingredientAmount) {
        this(
                new IngredientDto(ingredientAmount.getIngredient()),
                switch (ingredientAmount.getAmount()) {
                    case Amount.Weight w -> w.value().asMilligrams();
                    case Amount.Volume v -> v.value().asMilliliters();
                    case Amount.Individual i -> i.value().asQuantity();
                },
                switch (ingredientAmount.getAmount()) {
                    case Amount.Weight w -> IngredientAmountUnit.MILLIGRAM;
                    case Amount.Volume v -> IngredientAmountUnit.MILLILITER;
                    case Amount.Individual i -> IngredientAmountUnit.INDIVIDUAL;
                });
    }
}