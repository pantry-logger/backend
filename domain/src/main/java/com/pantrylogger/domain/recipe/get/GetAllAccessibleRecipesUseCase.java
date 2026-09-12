package com.pantrylogger.domain.recipe.get;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class GetAllAccessibleRecipesUseCase {

    private final RecipeRepositoryPort recipeRepositoryPort;

    public GetAllAccessibleRecipesUseCase(
            RecipeRepositoryPort recipeRepositoryPort
    ) {
        this.recipeRepositoryPort = recipeRepositoryPort;
    }

    public List<Recipe> execute(User user) {
        return recipeRepositoryPort.getAllAccessibleBy(user);
    }
}