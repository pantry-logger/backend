package com.pantrylogger.domain.recipe.ingredient.update;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.amount.Amount;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeNotFoundException;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
@Validated
public class UpdateIngredientAmountUseCase {
    private final RecipeRepositoryPort recipeRepository;

    public UpdateIngredientAmountUseCase(
            RecipeRepositoryPort recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public IngredientAmount execute(
            User user,
            RecipeUUID recipeUuid,
            UUID ingredientUuid,
            @Valid UpdateIngredientAmountCommand command
    ) {

        Recipe recipe = recipeRepository.getByUUID(recipeUuid)
                .orElseThrow(() -> new RecipeNotFoundException(recipeUuid));

        recipe.assertModifiableBy(user);

        recipe.updateIngredientAmount(
                new IngredientUUID(ingredientUuid),
                Amount.of(
                        command.amount(),
                        command.unit()
                )
        );

        recipeRepository.save(recipe);

        return recipe.getIngredients().stream()
                .filter(ia -> ia.getIngredient()
                        .getUuid()
                        .uuid()
                        .equals(ingredientUuid))
                .findFirst().orElseThrow();
    }

}