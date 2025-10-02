package com.pantrylogger.domain.ingredient;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GetAllIngredientsUseCase {

    private final IngredientRepositoryPort queryPort;

    public GetAllIngredientsUseCase(IngredientRepositoryPort queryPort) {
        this.queryPort = queryPort;
    }

    public List<Ingredient> getAllIngredients() {
        return queryPort.getAll();
    }
}