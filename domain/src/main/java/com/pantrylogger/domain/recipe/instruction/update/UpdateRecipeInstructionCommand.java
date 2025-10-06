package com.pantrylogger.domain.recipe.instruction.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRecipeInstructionCommand(
        @NotNull
        @NotBlank
        @Size(min = 10)
        String instruction
) {
}