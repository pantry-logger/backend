package com.pantrylogger.domain.recipe;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;

public class RecipeNotFoundException extends EntityNotFoundException {
    public RecipeNotFoundException(RecipeUUID recipeUUID) {
        super(String.format(
                "Recipe with Uuid %s not found.",
                recipeUUID.toString()
        ));
    }
}
