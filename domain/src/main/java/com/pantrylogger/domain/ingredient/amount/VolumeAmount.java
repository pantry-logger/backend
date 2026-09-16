package com.pantrylogger.domain.ingredient.amount;

import com.pantrylogger.domain.exception.AmountException;

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
            throw new AmountException("density must be positive");
        }

        return WeightAmount.fromMilligrams(asMilliliters() * densityMgPerMl);
    }

    public VolumeAmount plus(VolumeAmount other) {
        return new VolumeAmount(this.milliliters + other.milliliters);
    }

}