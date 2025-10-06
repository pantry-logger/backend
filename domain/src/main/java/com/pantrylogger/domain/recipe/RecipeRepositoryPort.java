package com.pantrylogger.domain.recipe;

import java.util.List;

import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;

public interface RecipeRepositoryPort {
    List<Recipe> getAll();

    Recipe getByUUID(RecipeUUID uuid);

    Recipe save(Recipe recipe);

    void delete(RecipeUUID uuid);
}