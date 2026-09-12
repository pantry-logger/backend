package com.pantrylogger.domain.recipe.instruction.add;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
@Validated
public class AddRecipeInstructionUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public AddRecipeInstructionUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeInstruction execute(
            User user,
            RecipeUUID recipeUuid,
            @Valid AddRecipeInstructionCommand addInstructionCommand
    ) {
        Recipe recipe = this.recipeRepository.getByUUID(
                        recipeUuid)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUuid));

        recipe.assertModifiableBy(user);

        recipe.addInstruction(new RecipeInstruction(
                addInstructionCommand.instruction()
        ));

        recipe = recipeRepository.save(recipe);

        List<RecipeInstruction> instructions = recipe.getInstructions();

        return recipe.getInstructions().get(instructions.size() - 1);
    }

}