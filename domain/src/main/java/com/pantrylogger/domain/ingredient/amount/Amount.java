package com.pantrylogger.domain.ingredient.amount;

import com.pantrylogger.domain.exception.AmountException;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;

public sealed interface Amount permits Amount.Weight, Amount.Volume, Amount.Individual {

    record Weight(WeightAmount value) implements Amount {
        @Override
        public int rawValue() {
            return value.asMilligrams();
        }
    }

    record Volume(VolumeAmount value) implements Amount {
        @Override
        public int rawValue() {
            return value.asMilliliters();
        }
    }

    record Individual(IndividualAmount value) implements Amount {
        @Override
        public int rawValue() {
            return value.asQuantity();
        }
    }

    int rawValue();

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

    @SuppressWarnings("checkstyle:Indentation")
    default Amount add(Amount other) {
        return switch (this) {
            case Weight(var w) -> switch (other) {
                case Weight(var wOther) -> new Weight(w.plus(wOther));
                case Volume _, Individual _ ->
                        throw incompatibleAddition(this, other);
            };
            case Volume(var v) -> switch (other) {
                case Volume(var vOther) -> new Volume(v.plus(vOther));
                case Weight _, Individual _ ->
                        throw incompatibleAddition(this, other);
            };
            case Individual(var i) -> switch (other) {
                case Individual(var iOther) -> new Individual(i.plus(iOther));
                case Weight _, Volume _ ->
                        throw incompatibleAddition(this, other);
            };
        };
    }

    private static AmountException incompatibleAddition(Amount a, Amount b) {
        return new AmountAdditionException(String.format(
                "Cannot add %s to %s",
                b.getClass().getSimpleName(),
                a.getClass().getSimpleName()
        ));
    }
}