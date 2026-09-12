package com.pantrylogger.domain.ingredient;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
public class GetIngredientByUuidUseCase {

    private final IngredientRepositoryPort ingredientRepository;

    public GetIngredientByUuidUseCase(IngredientRepositoryPort ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient execute(IngredientUUID uuid) {
        return ingredientRepository.getByUUID(uuid);
    }
}