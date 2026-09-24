package com.pantrylogger.domain.recipe.ingredient;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;

public class RecipeIngredientNotFoundException extends EntityNotFoundException {
    public RecipeIngredientNotFoundException(
            RecipeUUID recipeUUID,
            IngredientUUID ingredientUUID
    ) {
        super(String.format(
                "Recipe Ingredient with UUID %s not found on Recipe %s",
                ingredientUUID.toString(),
                recipeUUID.toString()
        ));
    }
}
