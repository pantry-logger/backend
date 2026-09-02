package com.pantrylogger.domain.recipe.create;

import java.util.ArrayList;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
@Validated
public class CreateRecipeUseCase {

    private final RecipeRepositoryPort recipeRepository;

    public CreateRecipeUseCase(RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe createRecipe(
            @Valid CreateRecipeCommand createRecipeCommand) {
        return this.recipeRepository.save(
                new Recipe(
                        new RecipeUUID(UUID.randomUUID()),
                        createRecipeCommand.name(),
                        createRecipeCommand.description(),
                        new ArrayList<>(),
                        new ArrayList<>()));

    }
}