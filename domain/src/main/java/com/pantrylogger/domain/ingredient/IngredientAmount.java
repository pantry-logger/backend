package com.pantrylogger.domain.ingredient;

public class IngredientAmount {

    private Ingredient ingredient;
    private int amount;
    private IngredientAmountUnit unit;

    public IngredientAmount(
            Ingredient ingredient,
            int amount,
            IngredientAmountUnit unit) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public IngredientAmountUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientAmountUnit unit) {
        this.unit = unit;
    }
}