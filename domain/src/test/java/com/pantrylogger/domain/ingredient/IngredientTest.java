package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class IngredientTest {
    private Ingredient ingredient;
    private IngredientUUID uuid = new IngredientUUID(UUID.randomUUID());
    private String ingredientName = "Carrot";
    private String ingredientDescriptioon = "Crunchy orange stick";

    @BeforeEach
    void setup() {
        this.ingredient = new Ingredient(
                this.uuid,
                this.ingredientName,
                this.ingredientDescriptioon);
    }

    @Test
    void ingredientGettersWork() {
        assertEquals(this.uuid, this.ingredient.getUuid());
        assertEquals(this.ingredientName, this.ingredient.getName());
        assertEquals(this.ingredientDescriptioon, this.ingredient.getDescription());
    }

    @Test
    void ingredientSettersWork() {
        String newName = "Baby Carrot";
        String newDescription = "Still Crunchy but now small";
        this.ingredient.setName(newName);
        this.ingredient.setDescription(newDescription);
        assertEquals(newName, this.ingredient.getName());
        assertEquals(newDescription, this.ingredient.getDescription());
    }

    @Test
    void testThatEqualsWorks() {
        Ingredient other = new Ingredient(
                this.uuid,
                this.ingredientName,
                this.ingredientDescriptioon);

        assertEquals(this.ingredient, other);
    }

}