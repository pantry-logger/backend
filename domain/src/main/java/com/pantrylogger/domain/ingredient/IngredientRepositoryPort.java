package com.pantrylogger.domain.ingredient;

import java.util.List;
import java.util.Optional;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

public interface IngredientRepositoryPort {

    List<Ingredient> getAll();

    Optional<Ingredient> getByUUID(IngredientUUID uuid);

    Ingredient save(Ingredient ingredient);

    void delete(IngredientUUID uuid);
}