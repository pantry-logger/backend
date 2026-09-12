package com.pantrylogger.domain.ingredient;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GetAllIngredientsUseCase {

    private final IngredientRepositoryPort ingredientRepository;

    public GetAllIngredientsUseCase(IngredientRepositoryPort ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> execute() {
        return ingredientRepository.getAll();
    }
}