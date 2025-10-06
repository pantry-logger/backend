package com.pantrylogger.domain.recipe.ingredient.move;

import jakarta.validation.constraints.NotNull;

public record MoveIngredientAmountCommand(
        @NotNull
        int toPos
) {
}