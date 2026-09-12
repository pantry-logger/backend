package com.pantrylogger.domain.recipe.get;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

class GetAllRecipesUseCaseTest {
    private GetAllRecipesUseCase getAllRecipesUseCase;

    private final List<Recipe> recipes = List.of(
            RecipeFixture.emptyRecipe(),
            RecipeFixture.privateEmptyRecipe()
    );

    @BeforeEach
    void setup() {
        RecipeRepositoryPort mockQueryPort = Mockito.mock(RecipeRepositoryPort.class);
        Mockito.when(mockQueryPort.getAll())
                .thenReturn(this.recipes);

        this.getAllRecipesUseCase = new GetAllRecipesUseCase(
                mockQueryPort);
    }

    @Test
    void getAllRecipesShouldFilterAllRecipeTwos() {
        var foundRecipes = this.getAllRecipesUseCase.execute();
        assertEquals(
                this.recipes,
                foundRecipes
        );
    }
}