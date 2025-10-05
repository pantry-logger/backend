package com.pantrylogger.domain.recipe.instruction.move;

import jakarta.validation.constraints.NotNull;

public record MoveRecipeInstructionCommand(
    @NotNull
    int toPos
) {
}