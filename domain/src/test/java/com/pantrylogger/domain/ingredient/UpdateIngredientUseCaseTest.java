package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.update.UpdateIngredientCommand;
import com.pantrylogger.domain.ingredient.update.UpdateIngredientUseCase;

class UpdateIngredientUseCaseTest {

    private UpdateIngredientUseCase updateIngredientUseCase;
    private final IngredientUUID badUUID = IngredientFixture.badUUID();
    private IngredientUUID ingredientUUID;
    private UpdateIngredientCommand updateIngredientCommand;
    private final Ingredient expectedIngredient = IngredientFixture.updated_carrot();

    @BeforeEach
    void setup() {
        IngredientRepositoryPort mockIngredientRepository = Mockito.mock(
                IngredientRepositoryPort.class);
        this.updateIngredientUseCase = new UpdateIngredientUseCase(
                mockIngredientRepository);

        var originalIngredient = IngredientFixture.carrot();

        this.ingredientUUID = originalIngredient.getUuid();
        this.updateIngredientCommand = new UpdateIngredientCommand(
                IngredientFixture.updated_carrot().getName(),
                IngredientFixture.updated_carrot().getDescription()
        );

        Mockito.when(mockIngredientRepository.getByUUID(this.ingredientUUID))
                .thenReturn(Optional.of(originalIngredient));

        doThrow(new EntityNotFoundException(String.format(
                "Ingredient with UUID %s not found",
                this.badUUID.uuid()
        ))).when(mockIngredientRepository).getByUUID(this.badUUID);

        Mockito.when(mockIngredientRepository.save(Mockito.any(Ingredient.class)))
                .thenReturn(this.expectedIngredient);
    }

    @Test
    void updateIngredientShouldSaveAndReturnUpdatedIngredient() {
        Ingredient updatedIngredient = this.updateIngredientUseCase.execute(
                this.ingredientUUID,
                this.updateIngredientCommand
        );

        assertEquals(
                this.expectedIngredient.getName(),
                updatedIngredient.getName()
        );
        assertEquals(
                this.expectedIngredient.getDescription(),
                updatedIngredient.getDescription()
        );
    }

    @Test
    void updateIngredientByUuidWithIncorrectUUIDShouldThrowException() {
        assertThrows(
                EntityNotFoundException.class, () -> {
                    this.updateIngredientUseCase.execute(
                            this.badUUID,
                            this.updateIngredientCommand
                    );
                }
        );
    }
}