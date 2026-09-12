package com.pantrylogger.domain.ingredient;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
@Validated
public class UpdateIngredientUseCase {
    private final IngredientRepositoryPort ingredientRepository;

    public UpdateIngredientUseCase(
            IngredientRepositoryPort ingredientRepository
    ) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient execute(
            IngredientUUID ingredientUUID,
            @Valid UpdateIngredientCommand updateIngredientCommand
    ) {
        Ingredient ingredient = this.ingredientRepository.getByUUID(
                ingredientUUID);
        ingredient.setName(updateIngredientCommand.name());
        ingredient.setDescription(updateIngredientCommand.description());
        return this.ingredientRepository.save(ingredient);
    }

}