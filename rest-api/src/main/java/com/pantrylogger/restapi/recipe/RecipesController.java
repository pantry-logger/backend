package com.pantrylogger.restapi.recipe;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.pantrylogger.domain.recipe.get.GetAllAccessibleRecipesUseCase;
import com.pantrylogger.domain.recipe.get.GetRecipeByUuidUseCase;
import com.pantrylogger.domain.recipe.update.UpdateRecipeCommand;
import com.pantrylogger.domain.recipe.update.UpdateRecipeUseCase;
import com.pantrylogger.restapi.SuccessResponse;
import com.pantrylogger.restapi.security.CustomUserDetails;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("recipes")
public class RecipesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            RecipesController.class);
    private final GetAllAccessibleRecipesUseCase getAllAccessibleRecipesUseCase;
    private final GetRecipeByUuidUseCase getRecipeByUuidUseCase;
    private final CreateRecipeUseCase createRecipeUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;
    private final DeleteRecipeUseCase deleteRecipeUseCase;

    public RecipesController(
            GetAllAccessibleRecipesUseCase getAllAccessibleRecipesUseCase,
            GetRecipeByUuidUseCase getRecipeByUuidUseCase,
            CreateRecipeUseCase createRecipeUseCase,
            UpdateRecipeUseCase updateRecipeUseCase,
            DeleteRecipeUseCase deleteRecipeUseCase
    ) {
        this.getAllAccessibleRecipesUseCase = getAllAccessibleRecipesUseCase;
        this.createRecipeUseCase = createRecipeUseCase;
        this.getRecipeByUuidUseCase = getRecipeByUuidUseCase;
        this.updateRecipeUseCase = updateRecipeUseCase;
        this.deleteRecipeUseCase = deleteRecipeUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RecipeDto>> findAllAccessible(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        LOGGER.debug("Getting all Accessible Recipes");

        return new ResponseEntity<>(
                this.getAllAccessibleRecipesUseCase.execute(principal.getUser())
                        .stream()
                        .map(RecipeDto::new)
                        .toList(), HttpStatus.OK
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecipeDto> createNew(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody CreateRecipeCommand createRecipeCommand
    ) {
        LOGGER.debug("Creating new Recipe {}", createRecipeCommand.name());

        return new ResponseEntity<>(
                new RecipeDto(this.createRecipeUseCase.execute(
                        principal.getUser(),
                        createRecipeCommand
                )),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecipeDto> findByUuid(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID uuid
    ) {
        LOGGER.debug("Getting recipe {}", uuid);

        return new ResponseEntity<>(
                new RecipeDto(this.getRecipeByUuidUseCase.execute(
                        principal.getUser(),
                        new RecipeUUID(uuid)
                )),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecipeDto> update(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID uuid,
            @RequestBody UpdateRecipeCommand updateRecipeCommand
    ) {
        LOGGER.debug("Updating Ingredient {}", uuid);

        return new ResponseEntity<>(
                new RecipeDto(this.updateRecipeUseCase.execute(
                        principal.getUser(),
                        new RecipeUUID(uuid),
                        updateRecipeCommand
                )), HttpStatus.OK
        );
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SuccessResponse> delete(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID uuid
    ) {
        LOGGER.debug("deleting recipe {}", uuid);

        this.deleteRecipeUseCase.deleteRecipe(
                principal.getUser(),
                new RecipeUUID(uuid)
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Recipe deleted"));
    }
}