package com.pantrylogger.domain.recipe.create;

import java.util.ArrayList;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
@Validated
public class CreateRecipeUseCase {

    private final RecipeRepositoryPort recipeRepository;

    public CreateRecipeUseCase(RecipeRepositoryPort recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe execute(
            User user,
            @Valid CreateRecipeCommand createRecipeCommand
    ) {
        return this.recipeRepository.save(
                new Recipe(
                        user.getUsername(),
                        createRecipeCommand.visibility(),
                        createRecipeCommand.name(),
                        createRecipeCommand.description(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ));

    }
}