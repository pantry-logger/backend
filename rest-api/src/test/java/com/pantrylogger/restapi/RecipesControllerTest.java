package com.pantrylogger.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.recipes.GetAllRecipesUseCase;

class RecipesControllerTest {
    private RecipesController recipesController;

    private List<String> getAllRecipes() {
        return List.of("Recipe1", "Recipe2", "Recipe3");
    }

    @BeforeEach
    void setup() {
        GetAllRecipesUseCase mockGetAllRecipesUseCase = Mockito.mock(GetAllRecipesUseCase.class);
        Mockito.when(mockGetAllRecipesUseCase.getAllRecipes())
                .thenReturn(getAllRecipes());

        this.recipesController = new RecipesController(
                mockGetAllRecipesUseCase);
    }

    @Test
    void findAllShouldReturnAllRecipesReturnedByUseCase() {
        var recipes = this.recipesController.findAll();
        assertEquals(this.getAllRecipes(), recipes);
    }
}