package com.pantrylogger.domain.recipe.instruction.delete;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
public class DeleteRecipeInstructionUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public DeleteRecipeInstructionUseCase(
            RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe deleteInstruction(
            UUID recipeUuid,
            UUID recipeInstructionUuid) {
        Recipe recipe = this.recipeRepository.getByUUID(new RecipeUUID(recipeUuid));

        recipe.deleteInstruction(
                new RecipeInstructionUUID(recipeInstructionUuid));

        recipe = recipeRepository.save(recipe);

        return recipe;
    }

}