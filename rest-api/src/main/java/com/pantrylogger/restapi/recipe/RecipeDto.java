package com.pantrylogger.restapi.recipe;

import java.util.List;
import java.util.UUID;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.restapi.ingredient.IngredientAmountDto;

public record RecipeDto(
        UUID uuid,
        String name,
        String description,
        List<IngredientAmountDto> ingredients,
        List<RecipeInstructionDto> instructions) {
    public RecipeDto(Recipe recipe) {
        this(
                recipe.getUuid().uuid(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getIngredients().stream().map(IngredientAmountDto::new).toList(),
                recipe.getInstructions().stream().map(RecipeInstructionDto::new).toList());
    }

}