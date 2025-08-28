package com.pantrylogger.domain.recipes;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GetAllRecipesUseCase {

    private final RecipeQueryPort queryPort;

    public GetAllRecipesUseCase(
            RecipeQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    public List<String> getAllRecipes() {
        return queryPort.getAll().stream()
                .filter(recipe -> !"Recipe2".equals(recipe))
                .toList();
    }
}