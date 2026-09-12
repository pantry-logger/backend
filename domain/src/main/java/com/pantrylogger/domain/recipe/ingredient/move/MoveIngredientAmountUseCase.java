package com.pantrylogger.domain.recipe.ingredient.move;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
@Validated
public class MoveIngredientAmountUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public MoveIngredientAmountUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public Recipe execute(
            User user,
            RecipeUUID recipeUuid,
            UUID ingredientUuid,
            @Valid MoveIngredientAmountCommand moveIngredientCommand
    ) {
        Recipe recipe = this.recipeRepository.getByUUID(
                        recipeUuid)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUuid));

        recipe.assertModifiableBy(user);

        recipe.moveIngredient(
                new IngredientUUID(ingredientUuid),
                moveIngredientCommand.toPos()
        );

        recipe = recipeRepository.save(recipe);

        return recipe;
    }

}