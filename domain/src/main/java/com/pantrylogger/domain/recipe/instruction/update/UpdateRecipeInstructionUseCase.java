package com.pantrylogger.domain.recipe.instruction.update;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
@Validated
public class UpdateRecipeInstructionUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public UpdateRecipeInstructionUseCase(
            RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeInstruction updateInstruction(
            UUID recipeUuid,
            UUID recipeInstructionUuid,
            @Valid UpdateRecipeInstructionCommand updateInstructionCommand) {
        Recipe recipe = this.recipeRepository.getByUUID(new RecipeUUID(recipeUuid));
        RecipeInstruction instructionToUpdate = recipe.getInstructions()
                .stream().filter(
                        instr -> instr.getUuid().equals(
                                new RecipeInstructionUUID(recipeInstructionUuid)))
                .findAny().orElseThrow(() -> new EntityNotFoundException(
                        String.format("Recipe Instruction with UUID %s not found on Recipe %s",
                                recipeInstructionUuid, recipeUuid)));
        instructionToUpdate.setInstruction(updateInstructionCommand.instruction());

        recipeRepository.save(recipe);

        return instructionToUpdate;
    }

}