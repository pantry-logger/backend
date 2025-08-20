package com.pantrylogger.postgresadapter;

import com.pantrylogger.domain.recipes.RecipeQueryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipesAdapter implements RecipeQueryPort {

    @Override
    public List<String> getAll() {
        return List.of(
            "Recipe1",
            "Recipe2",
            "Recipe3"
        );
    }
}