package com.pantrylogger.domain.ingredient.amount;

import com.pantrylogger.domain.exception.AmountException;

public record IndividualAmount(int quantity) {

    public static IndividualAmount of(int quantity) {
        return new IndividualAmount(quantity);
    }

    public int asQuantity() {
        return quantity;
    }

    public WeightAmount toWeight(int itemWeightMg) {
        if (itemWeightMg <= 0) {
            throw new AmountException("item weight must be positive");
        }

        return WeightAmount.fromMilligrams(asQuantity() * itemWeightMg);
    }

    public VolumeAmount toVolume(int itemVolumeMl) {
        if (itemVolumeMl <= 0) {
            throw new AmountException("Item volume must be positive");
        }

        return VolumeAmount.fromMilliliters(asQuantity() * itemVolumeMl);
    }

    public IndividualAmount plus(IndividualAmount other) {
        return new IndividualAmount(this.quantity + other.quantity);
    }
}