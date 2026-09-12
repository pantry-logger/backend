package com.pantrylogger.domain.recipe.get;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class GetRecipeByUuidUseCase {

    private final RecipeRepositoryPort recipeRepository;

    public GetRecipeByUuidUseCase(RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe execute(User user, RecipeUUID recipeUUID) {
        Recipe recipe = this.recipeRepository.getByUUID(
                        recipeUUID)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUUID));

        recipe.assertAccessibleBy(user);

        return recipe;
    }
}