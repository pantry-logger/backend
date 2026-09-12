package com.pantrylogger.domain.recipe;

import java.util.List;
import java.util.Optional;

import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.user.User;

public interface RecipeRepositoryPort {
    List<Recipe> getAll();

    List<Recipe> getAllAccessibleBy(User user);

    List<Recipe> getAllOwnedBy(User user);

    Optional<Recipe> getByUUID(RecipeUUID uuid);

    Recipe save(Recipe recipe);

    void delete(Recipe recipe);
}