package com.pantrylogger.domain.ingredient.amount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.pantrylogger.domain.ingredient.IngredientAmountUnit;
import com.pantrylogger.domain.ingredient.amount.Amount.Individual;
import com.pantrylogger.domain.ingredient.amount.Amount.Volume;
import com.pantrylogger.domain.ingredient.amount.Amount.Weight;

class AmountDomainTest {

    @Test
    void testVolumeRawValueOverride() {
        int amountValue = 2000;
        Amount amount = new Volume(new VolumeAmount(amountValue));
        assertEquals(amountValue, amount.rawValue());
    }

    @Test
    void testWeightRawValueOverride() {
        int amountValue = 200;
        Amount amount = new Weight(new WeightAmount(amountValue));
        assertEquals(amountValue, amount.rawValue());
    }

    @Test
    void testQuantityRawValueOverride() {
        int amountValue = 2;
        Amount amount = new Individual(new IndividualAmount(amountValue));
        assertEquals(amountValue, amount.rawValue());
    }

    @Test
    void testAmountOfWithVolume() {
        int amountValue = 2000;
        Amount amount = Amount.of(amountValue, IngredientAmountUnit.MILLILITER);
        assertEquals(amountValue, amount.rawValue());
        assertInstanceOf(Volume.class, amount);
    }

    @Test
    void testAmountOfWithLiters() {
        int amountValue = 2;
        Amount amount = Amount.of(amountValue, IngredientAmountUnit.LITER);
        assertInstanceOf(Volume.class, amount);
        assertEquals(amountValue * 1000, amount.rawValue());
    }

    @Test
    void testAmountOfWithWeight() {
        int amountValue = 200;
        Amount amount = Amount.of(amountValue, IngredientAmountUnit.MILLIGRAM);
        assertEquals(amountValue, amount.rawValue());
        assertInstanceOf(Weight.class, amount);
    }

    @Test
    void testAmountOfWithIndividual() {
        int amountValue = 2;
        Amount amount = Amount.of(amountValue, IngredientAmountUnit.INDIVIDUAL);
        assertEquals(amountValue, amount.rawValue());
        assertInstanceOf(Individual.class, amount);
    }

    @Test
    void testAddWeightAndWeightWorks() {
        int amountValue = 200;
        Amount amount = Amount.of(amountValue, IngredientAmountUnit.MILLIGRAM);
        amount = amount.add(Amount.of(
                amountValue,
                IngredientAmountUnit.MILLIGRAM
        ));

        assertEquals(amountValue * 2, amount.rawValue());
    }

    @Test
    void testAddWeightAndVolumeThrows() {
        int amountValue = 200;
        Amount amount1 = Amount.of(amountValue, IngredientAmountUnit.MILLIGRAM);
        Amount amount2 = Amount.of(
                amountValue,
                IngredientAmountUnit.MILLILITER
        );

        assertThrows(AmountAdditionException.class, () -> amount1.add(amount2));
    }

    @Test
    void testAddWeightAndIndividualThrows() {
        int amountValue = 200;
        Amount amount1 = Amount.of(amountValue, IngredientAmountUnit.GRAM);
        Amount amount2 = Amount.of(
                1,
                IngredientAmountUnit.INDIVIDUAL
        );

        assertThrows(AmountAdditionException.class, () -> amount1.add(amount2));
    }

    @Test
    void testAddVolumeAndVolumeWorks() {
        int amountValue = 200;
        Amount amount = Amount.of(
                amountValue,
                IngredientAmountUnit.MILLILITER
        );
        amount = amount.add(Amount.of(
                amountValue,
                IngredientAmountUnit.MILLILITER
        ));

        assertEquals(amountValue * 2, amount.rawValue());
    }

    @Test
    void testAddVolumeAndWeightThrows() {
        int amountValue = 200;
        Amount amount1 = Amount.of(
                amountValue,
                IngredientAmountUnit.MILLILITER
        );
        Amount amount2 = Amount.of(
                amountValue,
                IngredientAmountUnit.MILLIGRAM
        );

        assertThrows(AmountAdditionException.class, () -> amount1.add(amount2));
    }

    @Test
    void testAddVolumeAndIndividualThrows() {
        int amountValue = 200;
        Amount amount1 = Amount.of(
                amountValue,
                IngredientAmountUnit.MILLILITER
        );
        Amount amount2 = Amount.of(
                1,
                IngredientAmountUnit.INDIVIDUAL
        );

        assertThrows(AmountAdditionException.class, () -> amount1.add(amount2));
    }

    @Test
    void testAddIndividualAndIndividualWorks() {
        int amountValue = 2;
        Amount amount = Amount.of(
                amountValue,
                IngredientAmountUnit.INDIVIDUAL
        );
        amount = amount.add(Amount.of(
                amountValue,
                IngredientAmountUnit.INDIVIDUAL
        ));

        assertEquals(amountValue * 2, amount.rawValue());
    }

    @Test
    void testAddIndividualAndWeightThrows() {
        Amount amount1 = Amount.of(
                2,
                IngredientAmountUnit.INDIVIDUAL
        );
        Amount amount2 = Amount.of(
                200,
                IngredientAmountUnit.MILLIGRAM
        );

        assertThrows(AmountAdditionException.class, () -> amount1.add(amount2));
    }

    @Test
    void testAddIndividualAndVolumeThrows() {
        Amount amount1 = Amount.of(
                2,
                IngredientAmountUnit.INDIVIDUAL
        );
        Amount amount2 = Amount.of(
                200,
                IngredientAmountUnit.MILLILITER
        );

        assertThrows(AmountAdditionException.class, () -> amount1.add(amount2));
    }

}
