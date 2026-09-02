package com.pantrylogger.domain.recipe.instruction.add;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddRecipeInstructionCommand(
        @NotNull
        @NotBlank
        @Size(min = 10)
        String instruction
) {
}