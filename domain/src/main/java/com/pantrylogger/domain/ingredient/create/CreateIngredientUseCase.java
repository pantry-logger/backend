package com.pantrylogger.domain.ingredient.create;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;

@Component
@Validated
public class CreateIngredientUseCase {
    private final IngredientRepositoryPort ingredientRepository;

    public CreateIngredientUseCase(
            IngredientRepositoryPort ingredientRepository
    ) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient execute(
            @Valid CreateIngredientCommand createIngredientCommand
    ) {
        return this.ingredientRepository.save(
                new Ingredient(
                        new IngredientUUID(UUID.randomUUID()),
                        createIngredientCommand.name(),
                        createIngredientCommand.description()
                ));
    }

}