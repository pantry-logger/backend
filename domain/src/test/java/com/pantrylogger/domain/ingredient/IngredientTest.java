package com.pantrylogger.domain.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.pantrylogger.domain.IngredientFixture;

class IngredientTest {
    private Ingredient ingredient = IngredientFixture.carrot();

    @Test
    void ingredientGettersWork() {
        assertEquals(this.ingredient.getUuid(), this.ingredient.getUuid());
        assertEquals(this.ingredient.getName(), this.ingredient.getName());
        assertEquals(this.ingredient.getDescription(), this.ingredient.getDescription());
    }

    @Test
    void ingredientSettersWork() {
        String newName = IngredientFixture.updated_carrot().getName();
        String newDescription = IngredientFixture.updated_carrot().getName();
        this.ingredient.setName(newName);
        this.ingredient.setDescription(newDescription);
        assertEquals(newName, this.ingredient.getName());
        assertEquals(newDescription, this.ingredient.getDescription());
    }

    @Test
    void testThatEqualsWorks() {
        Ingredient other = new Ingredient(
                this.ingredient.getUuid(),
                this.ingredient.getName(),
                this.ingredient.getDescription());

        assertEquals(this.ingredient, other);
    }
}