package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class GetAllIngredientsUseCaseTest {

    private GetAllIngredientsUseCase getAllIngredientsUseCase;

    private List<Ingredient> getAllIngredients() {
        return List.of(
                new Ingredient(
                        new IngredientUUID(
                                UUID.fromString("b505467e-58ad-4c75-895a-baeea3ec15b6")),
                        "Tomato",
                        "Fresh red tomato"),
                new Ingredient(
                        new IngredientUUID(
                                UUID.fromString("b505467e-58ad-4c85-895a-baeea3ec15b6")),
                        "Cherry Tomato",
                        "Small sweet tomato"));

    }

    @BeforeEach
    void setup() {
        IngredientRepositoryPort mockIngredientRepositoryPort = Mockito.mock(IngredientRepositoryPort.class);
        Mockito.when(mockIngredientRepositoryPort.getAll()).thenReturn(getAllIngredients());

        this.getAllIngredientsUseCase = new GetAllIngredientsUseCase(
                mockIngredientRepositoryPort);
    }

    @Test
    void getAllIngredientsShouldReturn2Ingredients() {
        var ingredients = this.getAllIngredientsUseCase.getAllIngredients();

        assertEquals(2, ingredients.size());
        assertEquals("Tomato", ingredients.get(0).getName());
        assertEquals("Cherry Tomato", ingredients.get(1).getName());
    }
}