package com.pantrylogger.domain.ingredient.amount;

import com.pantrylogger.domain.exception.AmountConversionException;

public record VolumeAmount(int milliliters) {

    public static VolumeAmount fromLiters(double l) {
        return new VolumeAmount((int) (l * 1000));
    }

    public static VolumeAmount fromMilliliters(int ml) {
        return new VolumeAmount(ml);
    }

    public double asLiters() {
        return milliliters / 1000.0;
    }

    public int asMilliliters() {
        return milliliters;
    }

    public WeightAmount toWeight(int densityMgPerMl) {
        if (densityMgPerMl <= 0) {
            throw new AmountConversionException("density must be positive");
        }

        return WeightAmount.fromGrams(asMilliliters() * densityMgPerMl);
    }

}