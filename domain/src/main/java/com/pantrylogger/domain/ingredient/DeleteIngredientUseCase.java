package com.pantrylogger.domain.ingredient;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
public class DeleteIngredientUseCase {

    private final IngredientRepositoryPort ingredientRepository;

    public DeleteIngredientUseCase(
            IngredientRepositoryPort ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public void deleteIngredient(
            UUID uuid) {
        this.ingredientRepository.delete(new IngredientUUID(uuid));
    }

}