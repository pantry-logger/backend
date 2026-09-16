package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.ingredient.get.GetAllIngredientsUseCase;

class GetAllIngredientsUseCaseTest {

    private GetAllIngredientsUseCase getAllIngredientsUseCase;

    private List<Ingredient> ingredients = List.of(
            IngredientFixture.carrot(),
            IngredientFixture.tomato()
    );

    @BeforeEach
    void setup() {
        IngredientRepositoryPort mockIngredientRepositoryPort = Mockito.mock(
                IngredientRepositoryPort.class);
        Mockito.when(mockIngredientRepositoryPort.getAll())
                .thenReturn(this.ingredients);

        this.getAllIngredientsUseCase = new GetAllIngredientsUseCase(
                mockIngredientRepositoryPort);
    }

    @Test
    void getAllIngredientsShouldReturn2Ingredients() {
        var foundIngredients = this.getAllIngredientsUseCase.execute();

        assertEquals(2, foundIngredients.size());
        assertEquals(
                this.ingredients.get(0).getName(),
                foundIngredients.get(0).getName()
        );
        assertEquals(
                this.ingredients.get(1).getName(),
                foundIngredients.get(1).getName()
        );
    }
}