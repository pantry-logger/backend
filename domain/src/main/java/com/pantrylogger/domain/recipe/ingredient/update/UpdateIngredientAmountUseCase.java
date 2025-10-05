package com.pantrylogger.domain.recipe.ingredient.update;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

import jakarta.validation.Valid;

@Service
@Validated
public class UpdateIngredientAmountUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public UpdateIngredientAmountUseCase(
            RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public IngredientAmount updateIngredient(
            UUID recipeUuid,
            UUID ingredientUuid,
            @Valid UpdateIngredientAmountCommand command) {

        Recipe recipe = recipeRepository.getByUUID(new RecipeUUID(recipeUuid));

        recipe.updateIngredientAmount(
                new IngredientUUID(ingredientUuid),
                command.amount(),
                command.unit());

        recipeRepository.save(recipe);

        return recipe.getIngredients().stream()
                .filter(ia -> ia.getIngredient().getUuid().uuid().equals(ingredientUuid))
                .findFirst().orElseThrow();
    }

}