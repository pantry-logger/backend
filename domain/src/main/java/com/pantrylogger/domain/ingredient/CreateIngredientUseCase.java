package com.pantrylogger.domain.ingredient;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Component
@Validated
public class CreateIngredientUseCase {
    private final IngredientRepositoryPort ingredientRepository;

    public CreateIngredientUseCase(
            IngredientRepositoryPort ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient createIngredient(
             @Valid CreateIngredientCommand createIngredientCommand) {
        return this.ingredientRepository.save(
                new Ingredient(
                        new IngredientUUID(UUID.randomUUID()),
                        createIngredientCommand.name(),
                        createIngredientCommand.description()));
    }

}