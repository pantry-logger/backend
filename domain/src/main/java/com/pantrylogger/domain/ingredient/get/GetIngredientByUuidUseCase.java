package com.pantrylogger.domain.ingredient.get;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientNotFoundException;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;

@Component
public class GetIngredientByUuidUseCase {

    private final IngredientRepositoryPort ingredientRepository;

    public GetIngredientByUuidUseCase(IngredientRepositoryPort ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient execute(IngredientUUID uuid) {
        return ingredientRepository.getByUUID(uuid)
                .orElseThrow(() -> new IngredientNotFoundException(uuid));
    }
}