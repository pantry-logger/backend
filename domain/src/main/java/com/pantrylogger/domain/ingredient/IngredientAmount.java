package com.pantrylogger.domain.ingredient;

import com.pantrylogger.domain.ingredient.amount.Amount;

public class IngredientAmount {

    private Ingredient ingredient;
    private Amount amount;

    public IngredientAmount(
            Ingredient ingredient,
            Amount amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

}