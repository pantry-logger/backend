package com.pantrylogger.domain.recipe.instruction;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;

public class RecipeInstructionNotFoundException extends EntityNotFoundException {
    public RecipeInstructionNotFoundException(
            RecipeUUID recipeUUID,
            RecipeInstructionUUID recipeInstructionUUID
    ) {
        super(String.format(
                "Recipe Instruction with UUID %s not found on Recipe %s",
                recipeInstructionUUID.toString(),
                recipeUUID.toString()
        ));
    }
}
