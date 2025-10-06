package com.pantrylogger.domain.recipe.instruction.move;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
@Validated
public class MoveRecipeInstructionUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public MoveRecipeInstructionUseCase(
            RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe moveInstruction(
            UUID recipeUuid,
            UUID recipeInstructionUuid,
            @Valid MoveRecipeInstructionCommand moveInstructionCommand) {
        Recipe recipe = this.recipeRepository.getByUUID(new RecipeUUID(recipeUuid));

        recipe.moveInstruction(
                new RecipeInstructionUUID(recipeInstructionUuid),
                moveInstructionCommand.toPos());

        recipe = recipeRepository.save(recipe);

        return recipe;
    }

}