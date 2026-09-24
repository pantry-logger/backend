package com.pantrylogger.restapi.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.create.CreateIngredientCommand;
import com.pantrylogger.domain.ingredient.create.CreateIngredientUseCase;
import com.pantrylogger.domain.ingredient.delete.DeleteIngredientUseCase;
import com.pantrylogger.domain.ingredient.get.GetAllIngredientsUseCase;
import com.pantrylogger.domain.ingredient.get.GetIngredientByUuidUseCase;
import com.pantrylogger.domain.ingredient.update.UpdateIngredientCommand;
import com.pantrylogger.domain.ingredient.update.UpdateIngredientUseCase;

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
        this.getAllIngredientsUseCase = mock(GetAllIngredientsUseCase.class);
        this.getIngredientByUuidUseCase = mock(
                GetIngredientByUuidUseCase.class);
        this.createIngredientUseCase = mock(CreateIngredientUseCase.class);
        this.updateIngredientUseCase = mock(UpdateIngredientUseCase.class);
        this.deleteIngredientUseCase = mock(DeleteIngredientUseCase.class);

        controller = new IngredientsController(
                this.getAllIngredientsUseCase,
                this.getIngredientByUuidUseCase,
                this.createIngredientUseCase,
                this.updateIngredientUseCase,
                this.deleteIngredientUseCase
        );
    }

    @Test
    void findAllShouldReturnAllIngredients() {
        when(this.getAllIngredientsUseCase.execute()).thenReturn(List.of(this.testIngredient));

        var response = this.controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(
                testIngredient.getName(),
                response.getBody().get(0).name()
        );
    }

    @Test
    void findByUuidShouldReturnIngredientIfExists() {
        when(this.getIngredientByUuidUseCase.execute(this.testIngredient.getUuid()))
                .thenReturn(this.testIngredient);

        var response = this.controller.findByUuid(this.testIngredient.getUuid()
                .uuid());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.testIngredient.getName(), response.getBody().name());
    }

    @Test
    void createNewShouldReturnCreatedIngredient() {
        CreateIngredientCommand command = new CreateIngredientCommand(
                IngredientFixture.created_tomato().getName(),
                IngredientFixture.created_tomato().getDescription()
        );
        Ingredient createdIngredient = IngredientFixture.created_tomato();

        when(this.createIngredientUseCase.execute(command)).thenReturn(
                createdIngredient);

        var response = this.controller.createNew(command);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdIngredient.getName(), response.getBody().name());
    }

    @Test
    void updateShouldReturnUpdatedIngredient() {
        UpdateIngredientCommand command = new UpdateIngredientCommand(
                IngredientFixture.updated_carrot().getName(),
                IngredientFixture.updated_carrot().getDescription()
        );

        Ingredient updatedIngredient = IngredientFixture.updated_carrot();
        when(this.updateIngredientUseCase.execute(
                this.testIngredient.getUuid(), command))
                .thenReturn(updatedIngredient);

        var response = this.controller.update(
                this.testIngredient.getUuid()
                        .uuid(), command
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedIngredient.getName(), response.getBody().name());
        assertEquals(
                updatedIngredient.getDescription(),
                response.getBody().description()
        );
    }

    @Test
    void DeleteShouldReturnOk() {
        doNothing().when(deleteIngredientUseCase)
                .execute(this.testIngredient.getUuid());

        var response = this.controller.delete(this.testIngredient.getUuid()
                .uuid());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}