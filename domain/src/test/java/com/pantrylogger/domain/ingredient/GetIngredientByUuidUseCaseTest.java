package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class GetIngredientByUuidUseCaseTest {

    private GetIngredientByUuidUseCase getIngredientByUuidUseCase;

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
        IngredientRepositoryPort mockIngredientRepositoryPort = Mockito.mock(IngredientRepositoryPort.class);
        Mockito
                .when(mockIngredientRepositoryPort.getByUUID(this.getIngredient().getUuid()))
                .thenReturn(getIngredient());

        Mockito.when(mockIngredientRepositoryPort
                .getByUUID(this.badUUID))
                .thenThrow(new EntityNotFoundException(
                        String.format("Ingredient with UUID %s not found", this.badUUID.uuid())));

        this.getIngredientByUuidUseCase = new GetIngredientByUuidUseCase(mockIngredientRepositoryPort);
    }

    @Test
    void getIngredientByUuidShouldReturnIngredientAsOptional() {
        var ingredient = this.getIngredientByUuidUseCase
                .getIngredientByUuid(this.getIngredient().getUuid());

        assertEquals(this.getIngredient(), ingredient);
    }

    @Test
    void getIngredientByUuidWithIncorrectUUIDShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.getIngredientByUuidUseCase
                    .getIngredientByUuid(badUUID);

        });
    }
}