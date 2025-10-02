package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class DeleteIngredientUseCaseTest {

    private DeleteIngredientUseCase deleteIngredientUseCase;

    private IngredientRepositoryPort mockIngredientRepository = Mockito.mock(IngredientRepositoryPort.class);
    private IngredientUUID badUUID = IngredientFixture.badUUID();
    private Ingredient ingredient = IngredientFixture.tomato();

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
                .deleteIngredient(this.ingredient.getUuid().uuid());
        verify(this.mockIngredientRepository, times(1)).delete(this.ingredient.getUuid());
    }

    @Test
    void deleteIngredientByUuidWithIncorrectUUIDShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.deleteIngredientUseCase
                    .deleteIngredient(badUUID.uuid());
        });
    }
}