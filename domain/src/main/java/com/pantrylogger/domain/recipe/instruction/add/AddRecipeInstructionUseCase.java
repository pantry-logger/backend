package com.pantrylogger.domain.recipe.instruction.add;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

import jakarta.validation.Valid;

@Service
@Validated
public class AddRecipeInstructionUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public AddRecipeInstructionUseCase(
            RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeInstruction addInstruction(
            UUID recipeUuid,
            @Valid AddRecipeInstructionCommand addInstructionCommand) {
        Recipe recipe = this.recipeRepository.getByUUID(new RecipeUUID(recipeUuid));
        recipe.addInstruction(new RecipeInstruction(
                new RecipeInstructionUUID(UUID.randomUUID()),
                addInstructionCommand.instruction()));

        recipe = recipeRepository.save(recipe);

        List<RecipeInstruction> instructions = recipe.getInstructions();

        return recipe.getInstructions().get(instructions.size() - 1);
    }

}