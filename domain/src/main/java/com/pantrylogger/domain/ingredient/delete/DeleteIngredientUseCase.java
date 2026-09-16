package com.pantrylogger.domain.ingredient.delete;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;

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