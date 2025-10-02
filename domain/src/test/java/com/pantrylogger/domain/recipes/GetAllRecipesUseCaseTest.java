package com.pantrylogger.domain.recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetAllRecipesUseCaseTest {
    private GetAllRecipesUseCase getAllRecipesUseCase;

    private List<String> getAllRecipes() {
        return List.of("Recipe1", "Recipe2", "Recipe3");
    }

    @BeforeEach
    void setup() {
        RecipeQueryPort mockQueryPort = Mockito.mock(RecipeQueryPort.class);
        Mockito.when(mockQueryPort.getAll())
                .thenReturn(getAllRecipes());

        this.getAllRecipesUseCase = new GetAllRecipesUseCase(
                mockQueryPort);
    }

    @Test
    void getAllRecipesShouldFilterAllRecipeTwos() {
        var recipes = this.getAllRecipesUseCase.getAllRecipes();
        assertEquals(
                List.of("Recipe1", "Recipe3"),
                recipes);
    }
}