package com.pantrylogger.domain.ingredient;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
public class CreateIngredientUseCase {
    private final IngredientRepositoryPort ingredientRepository;

    public CreateIngredientUseCase(
            IngredientRepositoryPort ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient createIngredient(
            CreateIngredientCommand createIngredientCommand) {
        return this.ingredientRepository.save(
                new Ingredient(
                        new IngredientUUID(UUID.randomUUID()),
                        createIngredientCommand.name(),
                        createIngredientCommand.description()));
    }

}