package com.pantrylogger.postgresadapter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipesAdapterTest {
    @Test
    void getAllReturnsThreeRecipes() {
        var adapter = new RecipesAdapter();

        var result = adapter.getAll();
        assertEquals(
                List.of(
                    "Recipe1",
                    "Recipe2",
                    "Recipe3"
                ),
                result
        );
    }
}