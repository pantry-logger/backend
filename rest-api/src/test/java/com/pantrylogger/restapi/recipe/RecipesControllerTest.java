package com.pantrylogger.restapi.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.create.CreateRecipeCommand;
import com.pantrylogger.domain.recipe.create.CreateRecipeUseCase;
import com.pantrylogger.domain.recipe.delete.DeleteRecipeUseCase;
import com.pantrylogger.domain.recipe.get.GetAllAccessibleRecipesUseCase;
import com.pantrylogger.domain.recipe.get.GetAllRecipesUseCase;
import com.pantrylogger.domain.recipe.get.GetRecipeByUuidUseCase;
import com.pantrylogger.domain.recipe.update.UpdateRecipeCommand;
import com.pantrylogger.domain.recipe.update.UpdateRecipeUseCase;
import com.pantrylogger.restapi.security.CustomUserDetails;

class RecipesControllerTest {
    private CustomUserDetails testUserPrincipal = new CustomUserDetails(
            UserFixture.basicTestUser(),
            Set.of()
    );

    private RecipesController recipesController;

    private final GetAllAccessibleRecipesUseCase getAllAccessibleRecipesUseCase = mock(
            GetAllAccessibleRecipesUseCase.class);
    private final GetRecipeByUuidUseCase getRecipeByUuidUseCase = mock(
            GetRecipeByUuidUseCase.class);
    private final CreateRecipeUseCase createRecipeUseCase = mock(
            CreateRecipeUseCase.class);
    private final UpdateRecipeUseCase updateRecipeUseCase = mock(
            UpdateRecipeUseCase.class);
    private final DeleteRecipeUseCase deleteRecipeUseCase = mock(
            DeleteRecipeUseCase.class);

    private final List<Recipe> testRecipes = List.of(
            RecipeFixture.emptyRecipe(),
            RecipeFixture.privateEmptyRecipe()
    );

    private final Recipe updatedRecipe = RecipeFixture.updatedEmptyRecipe();

    @BeforeEach
    void setup() {
        GetAllRecipesUseCase mockGetAllRecipesUseCase = mock(
                GetAllRecipesUseCase.class);
        when(mockGetAllRecipesUseCase.execute()).thenReturn(this.testRecipes);

        when(this.getAllAccessibleRecipesUseCase.execute(this.testUserPrincipal.getUser())).thenReturn(
                this.testRecipes);
        when(this.getRecipeByUuidUseCase.execute(
                this.testUserPrincipal.getUser(),
                this.testRecipes.getFirst().getUuid()
        )).thenReturn(this.testRecipes.getFirst());

        this.recipesController = new RecipesController(
                this.getAllAccessibleRecipesUseCase,
                this.getRecipeByUuidUseCase,
                this.createRecipeUseCase,
                this.updateRecipeUseCase,
                this.deleteRecipeUseCase
        );
    }

    @Test
    void findAllShouldReturnAllRecipes() {

        var response = this.recipesController.findAllAccessible(this.testUserPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(
                testRecipes.get(0).getName(),
                response.getBody().get(0).name()
        );
    }

    @Test
    void findByUuidShouldReturnRecipeIfExists() {
        var response = this.recipesController.findByUuid(
                this.testUserPrincipal,
                this.testRecipes.getFirst().getUuid().uuid()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(
                this.testRecipes.getFirst().getName(),
                response.getBody().name()
        );
    }

    @Test
    void createNewShouldReturnCreatedRecipe() {
        CreateRecipeCommand command = new CreateRecipeCommand(
                this.testRecipes.getFirst().getName(),
                this.testRecipes.getFirst().getDescription(),
                this.testRecipes.getFirst().getVisibility()
        );
        Recipe createdRecipe = testRecipes.getFirst();

        when(this.createRecipeUseCase.execute(
                this.testUserPrincipal.getUser(),
                command
        )).thenReturn(createdRecipe);

        var response = this.recipesController.createNew(
                this.testUserPrincipal,
                command
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(createdRecipe.getName(), response.getBody().name());
    }

    @Test
    void updateShouldReturnUpdatedRecipe() {
        UpdateRecipeCommand command = new UpdateRecipeCommand(
                this.updatedRecipe.getName(),
                this.updatedRecipe.getDescription(),
                this.updatedRecipe.getVisibility()
        );

        when(this.updateRecipeUseCase.execute(
                this.testUserPrincipal.getUser(),
                this.testRecipes.getFirst().getUuid(),
                command
        )).thenReturn(updatedRecipe);

        var response = this.recipesController.update(
                this.testUserPrincipal,
                this.testRecipes.getFirst().getUuid().uuid(),
                command
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(this.updatedRecipe.getName(), response.getBody().name());
        assertEquals(
                this.updatedRecipe.getDescription(),
                response.getBody().description()
        );
        assertEquals(
                this.updatedRecipe.getVisibility(),
                response.getBody().recipeVisibility()
        );
    }

    @Test
    @SuppressWarnings("checkstyle:Indentation")
    void DeleteShouldReturnOk() {
        doNothing()
                .when(deleteRecipeUseCase)
                .deleteRecipe(
                        this.testUserPrincipal.getUser(),
                        this.testRecipes.getFirst().getUuid()
                );

        var response = this.recipesController.delete(
                this.testUserPrincipal,
                this.testRecipes.getFirst().getUuid().uuid()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}