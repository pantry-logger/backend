package com.pantrylogger.restapi.recipe;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.create.CreateRecipeCommand;
import com.pantrylogger.domain.recipe.create.CreateRecipeUseCase;
import com.pantrylogger.domain.recipe.delete.DeleteRecipeUseCase;
import com.pantrylogger.domain.recipe.get.GetAllRecipesUseCase;
import com.pantrylogger.domain.recipe.get.GetRecipeByUuidUseCase;
import com.pantrylogger.domain.recipe.update.UpdateRecipeCommand;
import com.pantrylogger.domain.recipe.update.UpdateRecipeUseCase;

@RestController
@RequestMapping("recipes")
public class RecipesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipesController.class);
    private final GetAllRecipesUseCase getAllRecipesUseCase;
    private final GetRecipeByUuidUseCase getRecipeByUuidUseCase;
    private final CreateRecipeUseCase createRecipeUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;
    private final DeleteRecipeUseCase deleteRecipeUseCase;

    public RecipesController(
            GetAllRecipesUseCase getAllRecipesUseCase,
            GetRecipeByUuidUseCase getRecipeByUuidUseCase,
            CreateRecipeUseCase createRecipeUseCase,
            UpdateRecipeUseCase updateRecipeUseCase,
            DeleteRecipeUseCase deleteRecipeUseCase
    ) {
        this.getAllRecipesUseCase = getAllRecipesUseCase;
        this.getRecipeByUuidUseCase = getRecipeByUuidUseCase;
        this.createRecipeUseCase = createRecipeUseCase;
        this.updateRecipeUseCase = updateRecipeUseCase;
        this.deleteRecipeUseCase = deleteRecipeUseCase;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDto>> findAll() {
        LOGGER.debug("Getting all Recipes");

        return new ResponseEntity<>(
                this.getAllRecipesUseCase.getAllRecipes()
                        .stream()
                        .map(RecipeDto::new)
                        .toList(),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RecipeDto> createNew(
            @RequestBody CreateRecipeCommand createRecipeCommand) {
        LOGGER.debug("Creating new Recipe {}", createRecipeCommand.name());

        return new ResponseEntity<>(
                new RecipeDto(this.createRecipeUseCase.createRecipe(createRecipeCommand)),
                HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RecipeDto> findByUuid(@PathVariable UUID uuid) {
        LOGGER.debug("Getting recipe {}", uuid);

        return new ResponseEntity<>(
                new RecipeDto(this.getRecipeByUuidUseCase.getRecipeByUuid(new RecipeUUID(uuid))),
                HttpStatus.OK);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<RecipeDto> update(
            @PathVariable UUID uuid,
            @RequestBody UpdateRecipeCommand updateRecipeCommand) {
        LOGGER.debug("Updating Ingredient {}", uuid);

        return new ResponseEntity<>(
                new RecipeDto(
                        this.updateRecipeUseCase.updateRecipe(
                                uuid, updateRecipeCommand)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(
            @PathVariable UUID uuid) {
        LOGGER.debug("deleting recipe {}", uuid);

        this.deleteRecipeUseCase.deleteRecipe(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("message", "Recipe deleted"));
    }
}