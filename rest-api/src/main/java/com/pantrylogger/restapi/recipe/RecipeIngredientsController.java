package com.pantrylogger.restapi.recipe;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pantrylogger.domain.recipe.ingredient.add.AddIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.add.AddIngredientAmountUseCase;
import com.pantrylogger.domain.recipe.ingredient.delete.DeleteIngredientAmountUseCase;
import com.pantrylogger.domain.recipe.ingredient.move.MoveIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.move.MoveIngredientAmountUseCase;
import com.pantrylogger.domain.recipe.ingredient.update.UpdateIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.update.UpdateIngredientAmountUseCase;
import com.pantrylogger.restapi.ingredient.IngredientAmountDto;

@RestController
@RequestMapping("recipes")
public class RecipeIngredientsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipesController.class);
    private final AddIngredientAmountUseCase addIngredientAmountUseCase;
    private final UpdateIngredientAmountUseCase updateIngredientAmountUseCase;
    private final MoveIngredientAmountUseCase moveIngredientAmountUseCase;
    private final DeleteIngredientAmountUseCase deleteIngredientAmountUseCase;

    public RecipeIngredientsController(
            AddIngredientAmountUseCase addIngredientAmountUseCase,
            UpdateIngredientAmountUseCase updateIngredientAmountUseCase,
            MoveIngredientAmountUseCase moveIngredientAmountUseCase,
            DeleteIngredientAmountUseCase deleteIngredientAmountUseCase) {
        this.addIngredientAmountUseCase = addIngredientAmountUseCase;
        this.updateIngredientAmountUseCase = updateIngredientAmountUseCase;
        this.moveIngredientAmountUseCase = moveIngredientAmountUseCase;
        this.deleteIngredientAmountUseCase = deleteIngredientAmountUseCase;

    }

    @PostMapping("/{recipeUuid}/ingredients")
    public ResponseEntity<IngredientAmountDto> addIngredient(
            @PathVariable UUID recipeUuid,
            @RequestBody AddIngredientAmountCommand addIngredientAmountCommand) {
        LOGGER.debug("Adding Ingredient to {}", recipeUuid);

        return new ResponseEntity<>(
                new IngredientAmountDto(
                        this.addIngredientAmountUseCase.addIngredient(
                                recipeUuid,
                                addIngredientAmountCommand)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{recipeUuid}/ingredients/{ingredientUuid}")
    public ResponseEntity<IngredientAmountDto> updateIngredient(
            @PathVariable UUID recipeUuid,
            @PathVariable UUID ingredientUuid,
            @RequestBody UpdateIngredientAmountCommand updateIngredientAmountCommand) {
        LOGGER.debug("Adding Ingredient to {}", recipeUuid);

        return new ResponseEntity<>(
                new IngredientAmountDto(
                        this.updateIngredientAmountUseCase.updateIngredient(
                                recipeUuid,
                                ingredientUuid,
                                updateIngredientAmountCommand)),
                HttpStatus.OK);
    }

    @PatchMapping("/{recipeUuid}/ingredients/{ingredientUuid}/position")
    public ResponseEntity<RecipeDto> moveIngredient(
            @PathVariable UUID recipeUuid,
            @PathVariable UUID ingredientUuid,
            @RequestBody MoveIngredientAmountCommand moveIngredientAmountCommand) {
        LOGGER.debug("moving Ingredient {} on {} to {}", ingredientUuid, recipeUuid,
                moveIngredientAmountCommand.toPos());

        return new ResponseEntity<>(
                new RecipeDto(
                        this.moveIngredientAmountUseCase.moveIngredient(
                                recipeUuid,
                                ingredientUuid,
                                moveIngredientAmountCommand)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{recipeUuid}/ingredients/{ingredientUuid}")
    public ResponseEntity<RecipeDto> deleteIngredient(
            @PathVariable UUID recipeUuid,
            @PathVariable UUID ingredientUuid) {
        LOGGER.debug("deleting Ingredient {} on {}", ingredientUuid, recipeUuid);

        return new ResponseEntity<>(
                new RecipeDto(
                        this.deleteIngredientAmountUseCase.deleteIngredient(
                                recipeUuid,
                                ingredientUuid)),
                HttpStatus.OK);
    }
}