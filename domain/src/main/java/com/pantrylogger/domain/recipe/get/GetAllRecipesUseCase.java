package com.pantrylogger.domain.recipe.get;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
public class GetAllRecipesUseCase {

    private final RecipeRepositoryPort recipeRepositoryPort;

    public GetAllRecipesUseCase(
            RecipeRepositoryPort recipeRepositoryPort) {
        this.recipeRepositoryPort = recipeRepositoryPort;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepositoryPort.getAll();
    }
}