package com.pantrylogger.postgresadapter;

import java.util.List;
import org.junit.jupiter.api.Test;

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