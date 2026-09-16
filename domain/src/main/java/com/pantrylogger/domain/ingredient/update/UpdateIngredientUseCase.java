package com.pantrylogger.domain.ingredient.update;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientNotFoundException;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;

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
                        ingredientUUID)
                .orElseThrow(() -> new IngredientNotFoundException(
                        ingredientUUID));
        ingredient.setName(updateIngredientCommand.name());
        ingredient.setDescription(updateIngredientCommand.description());
        return this.ingredientRepository.save(ingredient);
    }

}