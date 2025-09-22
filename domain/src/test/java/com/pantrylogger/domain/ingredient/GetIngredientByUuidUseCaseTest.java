package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class GetIngredientByUuidUseCaseTest {

    private GetIngredientByUuidUseCase getIngredientByUuidUseCase;

    private IngredientUUID badUUID = IngredientFixture.badUUID();
    private Ingredient ingredient = IngredientFixture.tomato();

    @BeforeEach
    void setup() {
        IngredientRepositoryPort mockIngredientRepositoryPort = Mockito.mock(IngredientRepositoryPort.class);
        Mockito
                .when(mockIngredientRepositoryPort.getByUUID(this.ingredient.getUuid()))
                .thenReturn(ingredient);

        Mockito.when(mockIngredientRepositoryPort
                .getByUUID(this.badUUID))
                .thenThrow(new EntityNotFoundException(
                        String.format("Ingredient with UUID %s not found", this.badUUID.uuid())));

        this.getIngredientByUuidUseCase = new GetIngredientByUuidUseCase(mockIngredientRepositoryPort);
    }

    @Test
    void getIngredientByUuidShouldReturnIngredientAsOptional() {
        var ingredient = this.getIngredientByUuidUseCase
                .getIngredientByUuid(this.ingredient.getUuid());

        assertEquals(this.ingredient, ingredient);
    }

    @Test
    void getIngredientByUuidWithIncorrectUUIDShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.getIngredientByUuidUseCase
                    .getIngredientByUuid(badUUID);

        });
    }
}