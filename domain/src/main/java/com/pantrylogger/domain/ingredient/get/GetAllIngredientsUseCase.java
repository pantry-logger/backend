package com.pantrylogger.domain.ingredient.get;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;

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