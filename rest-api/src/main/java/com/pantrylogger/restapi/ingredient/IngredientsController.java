package com.pantrylogger.restapi.ingredient;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.create.CreateIngredientCommand;
import com.pantrylogger.domain.ingredient.create.CreateIngredientUseCase;
import com.pantrylogger.domain.ingredient.delete.DeleteIngredientUseCase;
import com.pantrylogger.domain.ingredient.get.GetAllIngredientsUseCase;
import com.pantrylogger.domain.ingredient.get.GetIngredientByUuidUseCase;
import com.pantrylogger.domain.ingredient.update.UpdateIngredientCommand;
import com.pantrylogger.domain.ingredient.update.UpdateIngredientUseCase;
import com.pantrylogger.restapi.SuccessResponse;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("ingredients")
public class IngredientsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            IngredientsController.class);
    private final GetAllIngredientsUseCase getAllIngredientsUseCase;
    private final GetIngredientByUuidUseCase getIngredientByUuidUseCase;
    private final CreateIngredientUseCase createIngredientUseCase;
    private final UpdateIngredientUseCase updateIngredientUseCase;
    private final DeleteIngredientUseCase deleteIngredientUseCase;

    public IngredientsController(
            GetAllIngredientsUseCase getAllIngredientsUseCase,
            GetIngredientByUuidUseCase getIngredientByUuidUseCase,
            CreateIngredientUseCase createIngredientUseCase,
            UpdateIngredientUseCase updateIngredientUseCase,
            DeleteIngredientUseCase deleteIngredientUseCase
    ) {
        this.getAllIngredientsUseCase = getAllIngredientsUseCase;
        this.getIngredientByUuidUseCase = getIngredientByUuidUseCase;
        this.createIngredientUseCase = createIngredientUseCase;
        this.updateIngredientUseCase = updateIngredientUseCase;
        this.deleteIngredientUseCase = deleteIngredientUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IngredientDto>> findAll() {
        LOGGER.debug("Getting all Ingredients");

        return new ResponseEntity<>(
                this.getAllIngredientsUseCase.execute()
                        .stream()
                        .map(IngredientDto::new)
                        .toList(), HttpStatus.OK
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<IngredientDto> createNew(
            @RequestBody CreateIngredientCommand createIngredientCommand
    ) {
        LOGGER.debug(
                "Creating new ingredient {}",
                createIngredientCommand.name()
        );

        return new ResponseEntity<>(
                new IngredientDto(this.createIngredientUseCase.execute(
                        createIngredientCommand)), HttpStatus.CREATED
        );

    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<IngredientDto> findByUuid(
            @PathVariable UUID uuid
    ) {
        LOGGER.debug("Getting ingredient {}", uuid);

        return new ResponseEntity<>(
                new IngredientDto(this.getIngredientByUuidUseCase.execute(
                        new IngredientUUID(uuid))), HttpStatus.OK
        );
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngredientDto> update(
            @PathVariable UUID uuid,
            @RequestBody UpdateIngredientCommand updateIngredientCommand
    ) {
        LOGGER.debug("updating ingredient {}", uuid);

        return new ResponseEntity<>(
                new IngredientDto(this.updateIngredientUseCase.execute(
                        new IngredientUUID(uuid),
                        updateIngredientCommand
                )), HttpStatus.OK
        );

    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse> delete(
            @PathVariable UUID uuid
    ) {
        LOGGER.debug("deleting ingredient {}", uuid);

        this.deleteIngredientUseCase.execute(new IngredientUUID(uuid));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Ingredient deleted"));

    }
}