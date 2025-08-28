package com.pantrylogger.postgresadapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pantrylogger.domain.recipes.RecipeQueryPort;

@Component
public class RecipesAdapter implements RecipeQueryPort {

    @Override
    public List<String> getAll() {
        return List.of(
                "Recipe1",
                "Recipe2",
                "Recipe3");
    }
}
