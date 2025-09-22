package com.pantrylogger.restapi.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import com.pantrylogger.domain.ingredient.CreateIngredientCommand;
import com.pantrylogger.domain.ingredient.CreateIngredientUseCase;
import com.pantrylogger.domain.ingredient.DeleteIngredientUseCase;
import com.pantrylogger.domain.ingredient.GetAllIngredientsUseCase;
import com.pantrylogger.domain.ingredient.GetIngredientByUuidUseCase;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.UpdateIngredientCommand;
import com.pantrylogger.domain.ingredient.UpdateIngredientUseCase;

class IngredientsControllerTest {

    private IngredientsController controller;

    private GetAllIngredientsUseCase getAllIngredientsUseCase;
    private GetIngredientByUuidUseCase getIngredientByUuidUseCase;
    private CreateIngredientUseCase createIngredientUseCase;
    private UpdateIngredientUseCase updateIngredientUseCase;
    private DeleteIngredientUseCase deleteIngredientUseCase;

    private UUID ingredientUUID;
    private Ingredient testIngredient;

    @BeforeEach
    void setup() {
        this.getAllIngredientsUseCase = Mockito.mock(GetAllIngredientsUseCase.class);
        this.getIngredientByUuidUseCase = Mockito.mock(GetIngredientByUuidUseCase.class);
        this.createIngredientUseCase = Mockito.mock(CreateIngredientUseCase.class);
        this.updateIngredientUseCase = Mockito.mock(UpdateIngredientUseCase.class);
        this.deleteIngredientUseCase = Mockito.mock(DeleteIngredientUseCase.class);

        controller = new IngredientsController(
                this.getAllIngredientsUseCase,
                this.getIngredientByUuidUseCase,
                this.createIngredientUseCase,
                this.updateIngredientUseCase,
                this.deleteIngredientUseCase);

        ingredientUUID = UUID.randomUUID();
        testIngredient = new Ingredient(
                new IngredientUUID(ingredientUUID),
                "Salt",
                "Tastes like the sea");
    }

    @Test
    void findAllShouldReturnAllIngredients() {
        when(this.getAllIngredientsUseCase.getAllIngredients()).thenReturn(List.of(this.testIngredient));

        var response = this.controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Salt", response.getBody().get(0).name());
    }

    @Test
    void findByUuidShouldReturnIngredientIfExists() {
        when(this.getIngredientByUuidUseCase.getIngredientByUuid(new IngredientUUID(this.ingredientUUID)))
                .thenReturn(this.testIngredient);

        var response = this.controller.findByUuid(this.ingredientUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Salt", response.getBody().name());
    }

    @Test
    void createNewShouldReturnCreatedIngredient() {
        CreateIngredientCommand command = new CreateIngredientCommand("Sugar", "Sweet");
        Ingredient createdIngredient = new Ingredient(
                new IngredientUUID(UUID.randomUUID()),
                "Sugar",
                "Sweet");

        when(this.createIngredientUseCase.createIngredient(command)).thenReturn(createdIngredient);

        var response = this.controller.createNew(command);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Sugar", response.getBody().name());
    }

    @Test
    void updateShouldReturnUpdatedIngredient() {
        UpdateIngredientCommand command = new UpdateIngredientCommand("Pepper", "Spicy");
        Ingredient updatedIngredient = new Ingredient(
                new IngredientUUID(this.ingredientUUID),
                "Pepper",
                "Spicy");

        when(this.updateIngredientUseCase.updateIngredient(this.ingredientUUID, command)).thenReturn(updatedIngredient);

        var response = this.controller.update(this.ingredientUUID, command);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pepper", response.getBody().name());
        assertEquals("Spicy", response.getBody().description());
    }

    @Test
    void DeleteShouldReturnOk() {
        doNothing().when(deleteIngredientUseCase).deleteIngredient(this.ingredientUUID);

        var response = this.controller.delete(this.ingredientUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}