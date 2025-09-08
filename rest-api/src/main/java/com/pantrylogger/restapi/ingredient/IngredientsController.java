package com.pantrylogger.restapi.ingredient;

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

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.CreateIngredientCommand;
import com.pantrylogger.domain.ingredient.CreateIngredientUseCase;
import com.pantrylogger.domain.ingredient.DeleteIngredientUseCase;
import com.pantrylogger.domain.ingredient.GetAllIngredientsUseCase;
import com.pantrylogger.domain.ingredient.GetIngredientByUuidUseCase;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.UpdateIngredientCommand;
import com.pantrylogger.domain.ingredient.UpdateIngredientUseCase;

@RestController
@RequestMapping("ingredients")
public class IngredientsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientsController.class);
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
            DeleteIngredientUseCase deleteIngredientUseCase) {
        this.getAllIngredientsUseCase = getAllIngredientsUseCase;
        this.getIngredientByUuidUseCase = getIngredientByUuidUseCase;
        this.createIngredientUseCase = createIngredientUseCase;
        this.updateIngredientUseCase = updateIngredientUseCase;
        this.deleteIngredientUseCase = deleteIngredientUseCase;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDto>> findAll() {
        LOGGER.debug("Getting all Ingredients");

        return new ResponseEntity<>(
                this.getAllIngredientsUseCase.getAllIngredients()
                        .stream()
                        .map(IngredientDto::new)
                        .toList(),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IngredientDto> createNew(
            @RequestBody CreateIngredientCommand createIngredientCommand) {
        LOGGER.debug("Creating new ingredient {}", createIngredientCommand.name());

        return new ResponseEntity<>(
                new IngredientDto(this.createIngredientUseCase
                        .createIngredient(createIngredientCommand)),
                HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<IngredientDto> findByUuid(@PathVariable UUID uuid) {
        LOGGER.debug("Getting ingredient {}", uuid);

        try {
            return new ResponseEntity<>(
                    new IngredientDto(
                            this.getIngredientByUuidUseCase
                                    .getIngredientByUuid(new IngredientUUID(uuid))),
                    HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<IngredientDto> update(
            @PathVariable UUID uuid,
            @RequestBody UpdateIngredientCommand updateIngredientCommand) {
        LOGGER.debug("updating ingredient {}", uuid);

        try {
            return new ResponseEntity<>(
                    new IngredientDto(
                            this.updateIngredientUseCase.updateIngredient(
                                    uuid, updateIngredientCommand)),
                    HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(
            @PathVariable UUID uuid) {
        LOGGER.debug("deleting ingredient {}", uuid);

        try {
            this.deleteIngredientUseCase.deleteIngredient(uuid);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of("message", "Ingredient deleted"));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

}