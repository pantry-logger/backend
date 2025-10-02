package com.pantrylogger.domain.ingredient;

import java.util.List;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

public interface IngredientRepositoryPort {

    List<Ingredient> getAll();

    Ingredient getByUUID(IngredientUUID uuid);

    Ingredient save(Ingredient ingredient);

    void delete(IngredientUUID uuid);
}