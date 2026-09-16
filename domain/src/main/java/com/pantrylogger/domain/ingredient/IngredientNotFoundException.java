package com.pantrylogger.domain.ingredient;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

public class IngredientNotFoundException extends EntityNotFoundException {
    public IngredientNotFoundException(IngredientUUID ingredientUUID) {
        super(String.format(
                "Ingredient with Uuid %s not found.",
                ingredientUUID.toString()
        ));
    }
}
