package com.pantrylogger.domain.recipe.instruction.move;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
@Validated
public class MoveRecipeInstructionUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public MoveRecipeInstructionUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public Recipe execute(
            User user,
            RecipeUUID recipeUuid,
            UUID recipeInstructionUuid,
            @Valid MoveRecipeInstructionCommand moveInstructionCommand
    ) {
        Recipe recipe = this.recipeRepository.getByUUID(
                        recipeUuid)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUuid));

        recipe.assertModifiableBy(user);

        recipe.moveInstruction(
                new RecipeInstructionUUID(recipeInstructionUuid),
                moveInstructionCommand.toPos()
        );

        recipe = recipeRepository.save(recipe);

        return recipe;
    }

}