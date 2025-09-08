package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class UpdateIngredientUseCaseTest {

    private UpdateIngredientUseCase updateIngredientUseCase;
    private UUID ingredientUUID;
    private UpdateIngredientCommand updateIngredientCommand;
    private Ingredient expectedIngredient;

    @BeforeEach
    void setup() {
        IngredientRepositoryPort mockIngredientRepository = Mockito.mock(IngredientRepositoryPort.class);
        this.updateIngredientUseCase = new UpdateIngredientUseCase(mockIngredientRepository);

        this.ingredientUUID = UUID.randomUUID();
        this.updateIngredientCommand = new UpdateIngredientCommand("Crunchy Carrot", "Even crunchier orange stick");

        this.expectedIngredient = new Ingredient(
                new IngredientUUID(ingredientUUID),
                "Crunchy Carrot",
                "Even crunchier orange stick");

        Mockito.when(mockIngredientRepository.save(Mockito.any(Ingredient.class)))
                .thenReturn(this.expectedIngredient);
    }

    @Test
    void updateIngredientShouldSaveAndReturnUpdatedIngredient() {
        Ingredient updatedIngredient = this.updateIngredientUseCase.updateIngredient(
                this.ingredientUUID,
                this.updateIngredientCommand);

        assertEquals(this.expectedIngredient.getName(), updatedIngredient.getName());
        assertEquals(this.expectedIngredient.getDescription(), updatedIngredient.getDescription());
    }
}