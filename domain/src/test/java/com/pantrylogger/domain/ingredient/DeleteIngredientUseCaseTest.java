package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class DeleteIngredientUseCaseTest {

    private DeleteIngredientUseCase deleteIngredientUseCase;

    private IngredientRepositoryPort mockIngredientRepository = Mockito.mock(IngredientRepositoryPort.class);
    private IngredientUUID badUUID = new IngredientUUID("3146cc20-3461-41be-8ae0-b3dc3aea47c3");

    private Ingredient getIngredient() {
        return new Ingredient(
                new IngredientUUID(
                        "b505467e-58ad-4c75-895a-baeea3ec15b6"),
                "Tomato",
                "Fresh red tomato");
    }

    @BeforeEach
    void setup() {
        doThrow(new EntityNotFoundException(
                String.format("Ingredient with UUID %s not found", this.badUUID.uuid())))
                .when(mockIngredientRepository)
                .delete(this.badUUID);

        this.deleteIngredientUseCase = new DeleteIngredientUseCase(mockIngredientRepository);
    }

    @Test
    void deleteIngredientByUuidShouldReturnIngredientAsOptional() {
        this.deleteIngredientUseCase
                .deleteIngredient(this.getIngredient().getUuid().uuid());
        verify(this.mockIngredientRepository, times(1)).delete(this.getIngredient().getUuid());
    }

    @Test
    void deleteIngredientByUuidWithIncorrectUUIDShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.deleteIngredientUseCase
                    .deleteIngredient(badUUID.uuid());
        });
    }
}