package com.pantrylogger.restapi.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.create.CreateRecipeCommand;
import com.pantrylogger.domain.recipe.create.CreateRecipeUseCase;
import com.pantrylogger.domain.recipe.delete.DeleteRecipeUseCase;
import com.pantrylogger.domain.recipe.get.GetAllRecipesUseCase;
import com.pantrylogger.domain.recipe.get.GetRecipeByUuidUseCase;
import com.pantrylogger.domain.recipe.update.UpdateRecipeCommand;
import com.pantrylogger.domain.recipe.update.UpdateRecipeUseCase;

class RecipesControllerTest {
    private RecipesController recipesController;

    private GetAllRecipesUseCase getAllRecipesUseCase = Mockito.mock(GetAllRecipesUseCase.class);
    private GetRecipeByUuidUseCase getRecipeByUuidUseCase = Mockito.mock(GetRecipeByUuidUseCase.class);
    private CreateRecipeUseCase createRecipeUseCase = Mockito.mock(CreateRecipeUseCase.class);
    private UpdateRecipeUseCase updateRecipeUseCase = Mockito.mock(UpdateRecipeUseCase.class);
    private DeleteRecipeUseCase deleteRecipeUseCase = Mockito.mock(DeleteRecipeUseCase.class);

    private List<Recipe> testRecipes = List.of(
            RecipeFixture.emptyRecipe(),
            RecipeFixture.anotherEmptyRecipe());

    @BeforeEach
    void setup() {
        GetAllRecipesUseCase mockGetAllRecipesUseCase = Mockito.mock(GetAllRecipesUseCase.class);
        when(mockGetAllRecipesUseCase.getAllRecipes())
                .thenReturn(this.testRecipes);

        when(this.getAllRecipesUseCase.getAllRecipes()).thenReturn(this.testRecipes);
        when(this.getRecipeByUuidUseCase.getRecipeByUuid(this.testRecipes.get(0).getUuid()))
                .thenReturn(this.testRecipes.get(0));

        this.recipesController = new RecipesController(
                this.getAllRecipesUseCase,
                this.getRecipeByUuidUseCase,
                this.createRecipeUseCase,
                this.updateRecipeUseCase,
                this.deleteRecipeUseCase);
    }

    @Test
    void findAllShouldReturnAllRecipes() {

        var response = this.recipesController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(testRecipes.get(0).getName(), response.getBody().get(0).name());
    }

    @Test
    void findByUuidShouldReturnRecipeIfExists() {

        var response = this.recipesController.findByUuid(
                this.testRecipes.get(0).getUuid().uuid());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.testRecipes.get(0).getName(), response.getBody().name());
    }

    @Test
    void createNewShouldReturnCreatedRecipe() {
        CreateRecipeCommand command = new CreateRecipeCommand(
                testRecipes.get(0).getName(),
                testRecipes.get(0).getDescription());
        Recipe createdRecipe = testRecipes.get(0);

        when(this.createRecipeUseCase.createRecipe(command)).thenReturn(createdRecipe);

        var response = this.recipesController.createNew(command);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdRecipe.getName(), response.getBody().name());
    }

    @Test
    void updateShouldReturnUpdatedRecipe() {
        UpdateRecipeCommand command = new UpdateRecipeCommand(
                RecipeFixture.updatedEmptyRecipe().getName(),
                RecipeFixture.updatedEmptyRecipe().getDescription());

        Recipe updatedRecipe = RecipeFixture.updatedEmptyRecipe();
        when(this.updateRecipeUseCase.updateRecipe(
                this.testRecipes.get(0).getUuid().uuid(), command))
                .thenReturn(updatedRecipe);

        var response = this.recipesController.update(this.testRecipes.get(0).getUuid().uuid(), command);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRecipe.getName(), response.getBody().name());
        assertEquals(updatedRecipe.getDescription(), response.getBody().description());
    }

    @Test
    void DeleteShouldReturnOk() {
        doNothing().when(deleteRecipeUseCase).deleteRecipe(this.testRecipes.get(0).getUuid().uuid());

        var response = this.recipesController.delete(this.testRecipes.get(0).getUuid().uuid());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}