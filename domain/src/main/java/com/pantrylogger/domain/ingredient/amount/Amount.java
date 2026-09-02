package com.pantrylogger.domain.ingredient.amount;

import com.pantrylogger.domain.ingredient.IngredientAmountUnit;

public sealed interface Amount
        permits Amount.Weight,
                Amount.Volume,
                Amount.Individual {

    record Weight(WeightAmount value) implements Amount { }

    record Volume(VolumeAmount value) implements Amount { }

    record Individual(IndividualAmount value) implements Amount { }

    static Amount of(int amount, IngredientAmountUnit unit) {
        return switch (unit) {
            case MILLIGRAM -> new Weight(WeightAmount.fromMilligrams(amount));
            case GRAM -> new Weight(WeightAmount.fromGrams(amount));
            case KILOGRAM -> new Weight(WeightAmount.fromKilograms(amount));
            case MILLILITER -> new Volume(VolumeAmount.fromMilliliters(amount));
            case LITER -> new Volume(VolumeAmount.fromLiters(amount));
            case INDIVIDUAL -> new Individual(IndividualAmount.of(amount));
        };
    }
}