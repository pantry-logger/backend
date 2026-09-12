package com.pantrylogger.domain.recipe.instruction.delete;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class DeleteRecipeInstructionUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public DeleteRecipeInstructionUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe execute(
            User user,
            RecipeUUID recipeUuid,
            UUID recipeInstructionUuid
    ) {
        Recipe recipe = this.recipeRepository.getByUUID(
                        recipeUuid)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUuid));

        recipe.assertModifiableBy(user);

        recipe.deleteInstruction(
                new RecipeInstructionUUID(recipeInstructionUuid));

        recipe = recipeRepository.save(recipe);

        return recipe;
    }

}