package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class CreateIngredientUseCaseTest {

    private CreateIngredientUseCase createIngredientUseCase;
    private CreateIngredientCommand createIngredientCommand =
            new CreateIngredientCommand("Carrot",
            "Crunchy orange stick");
    private Ingredient expectedIngredient = new Ingredient(
            new IngredientUUID(UUID.randomUUID()),
            "Carrot",
            "Crunchy orange stick");

    @BeforeEach
    void setup() {
        IngredientRepositoryPort mockIngredientRepository = Mockito.mock(IngredientRepositoryPort.class);
        this.createIngredientUseCase = new CreateIngredientUseCase(mockIngredientRepository);

        Mockito.when(mockIngredientRepository.save(Mockito.any(Ingredient.class)))
                .thenReturn(this.expectedIngredient);

    }

    @Test
    void createIngredientShouldSaveAndReturnIngredient() {
        Ingredient ingredient = this.createIngredientUseCase.createIngredient(this.createIngredientCommand);

        assertEquals("Carrot", ingredient.getName());
        assertEquals("Crunchy orange stick", ingredient.getDescription());
    }
}