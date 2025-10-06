package com.pantrylogger.domain.recipe.get;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
public class GetRecipeByUuidUseCase {

    private final RecipeRepositoryPort recipeRepositoryPort;

    public GetRecipeByUuidUseCase(RecipeRepositoryPort recipeRepositoryPort) {
        this.recipeRepositoryPort = recipeRepositoryPort;
    }

    public Recipe getRecipeByUuid(RecipeUUID uuid) {
        return recipeRepositoryPort.getByUUID(uuid);
    }
}