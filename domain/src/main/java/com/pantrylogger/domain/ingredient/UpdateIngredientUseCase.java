package com.pantrylogger.domain.ingredient;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
public class UpdateIngredientUseCase {
    private final IngredientRepositoryPort ingredientRepository;

    public UpdateIngredientUseCase(
            IngredientRepositoryPort ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient updateIngredient(
            UUID uuid,
            UpdateIngredientCommand updateIngredientCommand) {

        Ingredient ingredient = this.ingredientRepository.getByUUID(new IngredientUUID(uuid));
        ingredient.setName(updateIngredientCommand.name());
        ingredient.setName(updateIngredientCommand.description());
        return this.ingredientRepository.save(ingredient);
    }

}