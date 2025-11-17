package com.pantrylogger.domain.ingredient.amount;

import com.pantrylogger.domain.exception.AmountConversionException;

public record WeightAmount(int milliGrams) {

    public static WeightAmount fromKilograms(double kg) {
        return new WeightAmount((int) (kg * 1_000_000));
    }

    public static WeightAmount fromGrams(double g) {
        return new WeightAmount((int) (g * 1000));
    }

    public static WeightAmount fromMilligrams(int mg) {
        return new WeightAmount(mg);
    }

    public double asKilograms() {
        return milliGrams / 1_000_000.0;
    }

    public double asGrams() {
        return milliGrams / 1000.0;
    }

    public int asMilligrams() {
        return milliGrams;
    }

    public VolumeAmount toVolume(double densityMgPerMl) {
        if (densityMgPerMl <= 0) {
            throw new AmountConversionException("density must be positive");
        }

        return VolumeAmount.fromMilliliters((int) (asMilligrams() / densityMgPerMl));
    }
}