package com.pantrylogger.restapi.ingredient;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;
import com.pantrylogger.domain.ingredient.amount.Amount;
import com.pantrylogger.domain.ingredient.amount.Amount.Individual;
import com.pantrylogger.domain.ingredient.amount.Amount.Volume;
import com.pantrylogger.domain.ingredient.amount.Amount.Weight;

public record IngredientAmountDto(
        IngredientDto ingredient,
        int amount,
        IngredientAmountUnit unit) {

    public IngredientAmountDto(IngredientAmount ingredientAmount) {
        this(
                new IngredientDto(ingredientAmount.getIngredient()),
                switch (ingredientAmount.getAmount()) {
                    case Weight(var w) -> w.asMilligrams();
                    case Volume(var v) -> v.asMilliliters();
                    case Individual(var i) -> i.asQuantity();
                },
                switch (ingredientAmount.getAmount()) {
                    case Amount.Weight _ -> IngredientAmountUnit.MILLIGRAM;
                    case Amount.Volume _ -> IngredientAmountUnit.MILLILITER;
                    case Amount.Individual _ -> IngredientAmountUnit.INDIVIDUAL;
                }
        );
    }
}