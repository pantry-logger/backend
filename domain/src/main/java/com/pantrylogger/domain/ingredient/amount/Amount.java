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
            case Weight w -> switch (other) {
                case Weight w2 -> new Weight(w.value().plus(w2.value()));
                case Volume ignored -> throw incompatibleAddition(this, other);
                case Individual ignored ->
                        throw incompatibleAddition(this, other);
            };
            case Volume v -> switch (other) {
                case Volume v2 -> new Volume(v.value().plus(v2.value()));
                case Weight ignored -> throw incompatibleAddition(this, other);
                case Individual ignored ->
                        throw incompatibleAddition(this, other);
            };
            case Individual i -> switch (other) {
                case Individual i2 ->
                        new Individual(i.value().plus(i2.value()));
                case Weight ignored -> throw incompatibleAddition(this, other);
                case Volume ignored -> throw incompatibleAddition(this, other);
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