package com.pantrylogger.restapi.recipe;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.ingredient.add.AddIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.add.AddIngredientAmountUseCase;
import com.pantrylogger.domain.recipe.ingredient.delete.DeleteIngredientAmountUseCase;
import com.pantrylogger.domain.recipe.ingredient.move.MoveIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.move.MoveIngredientAmountUseCase;
import com.pantrylogger.domain.recipe.ingredient.update.UpdateIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.update.UpdateIngredientAmountUseCase;
import com.pantrylogger.restapi.ingredient.IngredientAmountDto;
import com.pantrylogger.restapi.security.CustomUserDetails;

@RestController
@RequestMapping("recipes")
@PreAuthorize("isAuthenticated()")
public class RecipeIngredientsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            RecipeIngredientsController.class);
    private final AddIngredientAmountUseCase addIngredientAmountUseCase;
    private final UpdateIngredientAmountUseCase updateIngredientAmountUseCase;
    private final MoveIngredientAmountUseCase moveIngredientAmountUseCase;
    private final DeleteIngredientAmountUseCase deleteIngredientAmountUseCase;

    public RecipeIngredientsController(
            AddIngredientAmountUseCase addIngredientAmountUseCase,
            UpdateIngredientAmountUseCase updateIngredientAmountUseCase,
            MoveIngredientAmountUseCase moveIngredientAmountUseCase,
            DeleteIngredientAmountUseCase deleteIngredientAmountUseCase
    ) {
        this.addIngredientAmountUseCase = addIngredientAmountUseCase;
        this.updateIngredientAmountUseCase = updateIngredientAmountUseCase;
        this.moveIngredientAmountUseCase = moveIngredientAmountUseCase;
        this.deleteIngredientAmountUseCase = deleteIngredientAmountUseCase;

    }

    @PostMapping("/{recipeUuid}/ingredients")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<IngredientAmountDto> addIngredient(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @RequestBody AddIngredientAmountCommand addIngredientAmountCommand
    ) {
        LOGGER.debug("Adding Ingredient to {}", recipeUuid);

        return new ResponseEntity<>(
                new IngredientAmountDto(
                        this.addIngredientAmountUseCase.execute(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                addIngredientAmountCommand
                        )),
                HttpStatus.CREATED
        );
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    @PatchMapping("/{recipeUuid}/ingredients/{ingredientUuid}")
    public ResponseEntity<IngredientAmountDto> updateIngredient(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @PathVariable UUID ingredientUuid,
            @RequestBody UpdateIngredientAmountCommand updateIngredientAmountCommand
    ) {
        LOGGER.debug("updating Ingredient of {}", recipeUuid);

        return new ResponseEntity<>(
                new IngredientAmountDto(
                        this.updateIngredientAmountUseCase.execute(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                ingredientUuid,
                                updateIngredientAmountCommand
                        )),
                HttpStatus.OK
        );
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    @PatchMapping("/{recipeUuid}/ingredients/{ingredientUuid}/position")
    public ResponseEntity<RecipeDto> moveIngredient(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @PathVariable UUID ingredientUuid,
            @RequestBody MoveIngredientAmountCommand moveIngredientAmountCommand
    ) {
        LOGGER.debug(
                "moving Ingredient {} on {} to {}", ingredientUuid, recipeUuid,
                moveIngredientAmountCommand.toPos()
        );

        return new ResponseEntity<>(
                new RecipeDto(
                        this.moveIngredientAmountUseCase.execute(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                ingredientUuid,
                                moveIngredientAmountCommand
                        )),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{recipeUuid}/ingredients/{ingredientUuid}")
    public ResponseEntity<RecipeDto> deleteIngredient(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @PathVariable UUID ingredientUuid
    ) {
        LOGGER.debug(
                "deleting Ingredient {} on {}",
                ingredientUuid,
                recipeUuid
        );

        return new ResponseEntity<>(
                new RecipeDto(
                        this.deleteIngredientAmountUseCase.deleteIngredient(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                ingredientUuid
                        )),
                HttpStatus.OK
        );
    }
}