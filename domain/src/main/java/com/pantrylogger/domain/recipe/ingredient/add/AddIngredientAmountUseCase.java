package com.pantrylogger.domain.recipe.ingredient.add;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
@Validated
public class AddIngredientAmountUseCase {
    private final RecipeRepositoryPort recipeRepository;
    private final IngredientRepositoryPort ingredientRepository;

    public AddIngredientAmountUseCase(
            RecipeRepositoryPort recipeRepository,
            IngredientRepositoryPort ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public IngredientAmount addIngredient(
            UUID recipeUuid,
            @Valid AddIngredientAmountCommand addIngredientAmountCommand) {
        Recipe recipe = this.recipeRepository.getByUUID(new RecipeUUID(recipeUuid));
        Ingredient ingredient = this.ingredientRepository.getByUUID(addIngredientAmountCommand.ingredientUUID());
        recipe.addIngredient(new IngredientAmount(
                ingredient,
                addIngredientAmountCommand.amount(),
                addIngredientAmountCommand.unit()));

        recipe = recipeRepository.save(recipe);

        List<IngredientAmount> ingredients = recipe.getIngredients();

        return recipe.getIngredients().get(ingredients.size() - 1);
    }

}