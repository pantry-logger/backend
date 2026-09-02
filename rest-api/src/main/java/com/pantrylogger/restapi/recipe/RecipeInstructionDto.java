package com.pantrylogger.restapi.recipe;

import java.util.UUID;

import com.pantrylogger.domain.recipe.RecipeInstruction;

public record RecipeInstructionDto(UUID uuid, String instruction) {
    public RecipeInstructionDto(RecipeInstruction recipeInstruction) {
        this(
                recipeInstruction.getUuid().uuid(),
                recipeInstruction.getInstruction());

    }
}