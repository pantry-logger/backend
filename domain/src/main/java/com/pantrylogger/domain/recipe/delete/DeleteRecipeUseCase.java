package com.pantrylogger.domain.recipe.delete;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Component
public class DeleteRecipeUseCase {

    private final RecipeRepositoryPort recipeRepository;

    public DeleteRecipeUseCase(
            RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void deleteRecipe(
            UUID uuid) {
        this.recipeRepository.delete(new RecipeUUID(uuid));
    }
}