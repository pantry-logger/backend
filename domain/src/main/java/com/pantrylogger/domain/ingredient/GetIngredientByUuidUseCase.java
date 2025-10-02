package com.pantrylogger.domain.ingredient;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
public class GetIngredientByUuidUseCase {

    private final IngredientRepositoryPort queryPort;

    public GetIngredientByUuidUseCase(IngredientRepositoryPort queryPort) {
        this.queryPort = queryPort;
    }

    public Ingredient getIngredientByUuid(IngredientUUID uuid) {
        return queryPort.getByUUID(uuid);
    }
}