package com.pantrylogger.domain.recipe.delete;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Component
public class DeleteRecipeUseCase {

    private final RecipeRepositoryPort recipeRepository;

    public DeleteRecipeUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    public void deleteRecipe(
            User user,
            RecipeUUID recipeUUID
    ) {
        Recipe recipe = this.recipeRepository.getByUUID(recipeUUID)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUUID));

        recipe.assertModifiableBy(user);

        this.recipeRepository.delete(recipe);
    }
}