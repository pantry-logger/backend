package com.pantrylogger.domain.recipe.update;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeAccessDeniedException;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Component
@Validated
public class UpdateRecipeUseCase {
    private static Logger LOGGER = LoggerFactory.getLogger(UpdateRecipeUseCase.class);

    private final RecipeRepositoryPort recipeRepository;

    public UpdateRecipeUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe execute(
            User user,
            RecipeUUID recipeUUID,
            @Valid UpdateRecipeCommand updateRecipeCommand
    ) {
        LOGGER.debug("-------");
        LOGGER.debug(user.getUsername().toString());
        Recipe recipe = this.recipeRepository.getByUUID(
                        recipeUUID)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUUID));

        LOGGER.debug(recipe.getOwner().toString());
        LOGGER.debug(recipe.getVisibility().toString());
        recipe.assertModifiableBy(user);

        if (!recipe.getOwner().equals(user.getUsername())) {
            throw new RecipeAccessDeniedException();
        }
        recipe.setName(updateRecipeCommand.name());
        recipe.setDescription(updateRecipeCommand.description());
        recipe.setVisibility(updateRecipeCommand.recipeVisibility());

        return this.recipeRepository.save(recipe);
    }
}