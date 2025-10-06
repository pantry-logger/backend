package com.pantrylogger.domain.recipe.ingredient.move;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
@Validated
public class MoveIngredientAmountUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public MoveIngredientAmountUseCase(
            RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe moveIngredient(
            UUID recipeUuid,
            UUID ingredientUuid,
            @Valid MoveIngredientAmountCommand moveIngredientCommand) {
        Recipe recipe = this.recipeRepository.getByUUID(new RecipeUUID(recipeUuid));

        recipe.moveIngredient(
                new IngredientUUID(ingredientUuid),
                moveIngredientCommand.toPos());

        recipe = recipeRepository.save(recipe);

        return recipe;
    }

}