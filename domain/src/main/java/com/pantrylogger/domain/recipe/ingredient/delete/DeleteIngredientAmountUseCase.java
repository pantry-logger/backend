package com.pantrylogger.domain.recipe.ingredient.delete;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class DeleteIngredientAmountUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public DeleteIngredientAmountUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe deleteIngredient(
            User user,
            RecipeUUID recipeUuid,
            UUID ingredientUuid
    ) {
        Recipe recipe = this.recipeRepository.getByUUID(
                        recipeUuid)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUuid));

        recipe.assertModifiableBy(user);

        recipe.deleteIngredient(
                new IngredientUUID(ingredientUuid));

        recipe = recipeRepository.save(recipe);

        return recipe;
    }

}