package com.pantrylogger.restapi.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.ingredient.CreateIngredientCommand;
import com.pantrylogger.domain.ingredient.CreateIngredientUseCase;
import com.pantrylogger.domain.ingredient.DeleteIngredientUseCase;
import com.pantrylogger.domain.ingredient.GetAllIngredientsUseCase;
import com.pantrylogger.domain.ingredient.GetIngredientByUuidUseCase;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.UpdateIngredientCommand;
import com.pantrylogger.domain.ingredient.UpdateIngredientUseCase;

class IngredientsControllerTest {

    private IngredientsController controller;

    private GetAllIngredientsUseCase getAllIngredientsUseCase;
    private GetIngredientByUuidUseCase getIngredientByUuidUseCase;
    private CreateIngredientUseCase createIngredientUseCase;
    private UpdateIngredientUseCase updateIngredientUseCase;
    private DeleteIngredientUseCase deleteIngredientUseCase;

    private Ingredient testIngredient = IngredientFixture.carrot();

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
    }

    @Test
    void findAllShouldReturnAllIngredients() {
        when(this.getAllIngredientsUseCase.getAllIngredients()).thenReturn(List.of(this.testIngredient));

        var response = this.controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testIngredient.getName(), response.getBody().get(0).name());
    }

    @Test
    void findByUuidShouldReturnIngredientIfExists() {
        when(this.getIngredientByUuidUseCase.getIngredientByUuid(this.testIngredient.getUuid()))
                .thenReturn(this.testIngredient);

        var response = this.controller.findByUuid(this.testIngredient.getUuid().uuid());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.testIngredient.getName(), response.getBody().name());
    }

    @Test
    void createNewShouldReturnCreatedIngredient() {
        CreateIngredientCommand command = new CreateIngredientCommand(
                IngredientFixture.created_tomato().getName(),
                IngredientFixture.created_tomato().getDescription());
        Ingredient createdIngredient = IngredientFixture.created_tomato();

        when(this.createIngredientUseCase.createIngredient(command)).thenReturn(createdIngredient);

        var response = this.controller.createNew(command);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdIngredient.getName(), response.getBody().name());
    }

    @Test
    void updateShouldReturnUpdatedIngredient() {
        UpdateIngredientCommand command = new UpdateIngredientCommand(
                IngredientFixture.updated_carrot().getName(),
                IngredientFixture.updated_carrot().getDescription());

        Ingredient updatedIngredient = IngredientFixture.updated_carrot();
        when(this.updateIngredientUseCase.updateIngredient(
                this.testIngredient.getUuid().uuid(), command))
                .thenReturn(updatedIngredient);

        var response = this.controller.update(this.testIngredient.getUuid().uuid(), command);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedIngredient.getName(), response.getBody().name());
        assertEquals(updatedIngredient.getDescription(), response.getBody().description());
    }

    @Test
    void DeleteShouldReturnOk() {
        doNothing().when(deleteIngredientUseCase).deleteIngredient(this.testIngredient.getUuid().uuid());

        var response = this.controller.delete(this.testIngredient.getUuid().uuid());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}