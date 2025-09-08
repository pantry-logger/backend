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
        return this.ingredientRepository.save(
                new Ingredient(
                        new IngredientUUID(uuid),
                        updateIngredientCommand.name(),
                        updateIngredientCommand.description()));
    }

}