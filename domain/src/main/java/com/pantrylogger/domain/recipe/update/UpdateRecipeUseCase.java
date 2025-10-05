package com.pantrylogger.domain.recipe.update;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;

import jakarta.validation.Valid;

@Component
@Validated
public class UpdateRecipeUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public UpdateRecipeUseCase(
        RecipeRepositoryPort recipeRepository){
        this.recipeRepository = recipeRepository;
    }

    public Recipe updateRecipe(
        UUID uuid,
        @Valid UpdateRecipeCommand updateRecipeCommand) {
        Recipe recipe = this.recipeRepository.getByUUID(new RecipeUUID(uuid));
        recipe.setName(updateRecipeCommand.name());
        recipe.setDescription(updateRecipeCommand.description());

        return this.recipeRepository.save(recipe);
    }
    
}