package com.pantrylogger.domain.ingredient;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
public class DeleteIngredientUseCase {

    private final IngredientRepositoryPort ingredientRepository;

    public DeleteIngredientUseCase(
            IngredientRepositoryPort ingredientRepository
    ) {
        this.ingredientRepository = ingredientRepository;
    }

    public void execute(
            IngredientUUID ingredientUUID
    ) {
        this.ingredientRepository.delete(ingredientUUID);
    }

}