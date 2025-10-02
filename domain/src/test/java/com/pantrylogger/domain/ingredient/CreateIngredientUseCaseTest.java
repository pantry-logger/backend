package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.IngredientFixture;

class CreateIngredientUseCaseTest {

    private CreateIngredientUseCase createIngredientUseCase;
    private CreateIngredientCommand createIngredientCommand = new CreateIngredientCommand(
            IngredientFixture.carrot().getName(),
            IngredientFixture.carrot().getDescription());
    private Ingredient expectedIngredient = IngredientFixture.carrot();

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

        assertEquals(createIngredientCommand.name(), ingredient.getName());
        assertEquals(createIngredientCommand.description(), ingredient.getDescription());
    }
}