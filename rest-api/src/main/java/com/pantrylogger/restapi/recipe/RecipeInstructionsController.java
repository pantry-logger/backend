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
import com.pantrylogger.domain.recipe.instruction.add.AddRecipeInstructionCommand;
import com.pantrylogger.domain.recipe.instruction.add.AddRecipeInstructionUseCase;
import com.pantrylogger.domain.recipe.instruction.delete.DeleteRecipeInstructionUseCase;
import com.pantrylogger.domain.recipe.instruction.move.MoveRecipeInstructionCommand;
import com.pantrylogger.domain.recipe.instruction.move.MoveRecipeInstructionUseCase;
import com.pantrylogger.domain.recipe.instruction.update.UpdateRecipeInstructionCommand;
import com.pantrylogger.domain.recipe.instruction.update.UpdateRecipeInstructionUseCase;
import com.pantrylogger.restapi.security.CustomUserDetails;

@RestController
@RequestMapping("recipes")
@PreAuthorize("isAuthenticated()")
public class RecipeInstructionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            RecipeInstructionsController.class);
    private final AddRecipeInstructionUseCase addRecipeInstructionUseCase;
    private final UpdateRecipeInstructionUseCase updateRecipeInstructionUseCase;
    private final MoveRecipeInstructionUseCase moveRecipeInstructionUseCase;
    private final DeleteRecipeInstructionUseCase deleteRecipeInstructionUseCase;

    public RecipeInstructionsController(
            AddRecipeInstructionUseCase addRecipeInstructionUseCase,
            UpdateRecipeInstructionUseCase updateRecipeInstructionUseCase,
            MoveRecipeInstructionUseCase moveRecipeInstructionUseCase,
            DeleteRecipeInstructionUseCase deleteRecipeInstructionUseCase
    ) {
        this.addRecipeInstructionUseCase = addRecipeInstructionUseCase;
        this.updateRecipeInstructionUseCase = updateRecipeInstructionUseCase;
        this.moveRecipeInstructionUseCase = moveRecipeInstructionUseCase;
        this.deleteRecipeInstructionUseCase = deleteRecipeInstructionUseCase;
    }

    @PostMapping("/{recipeUuid}/instructions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecipeInstructionDto> addInstruction(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @RequestBody AddRecipeInstructionCommand addRecipeInstructionCommand
    ) {
        LOGGER.debug("Adding Instruction to {}", recipeUuid);

        return new ResponseEntity<>(
                new RecipeInstructionDto(
                        this.addRecipeInstructionUseCase.execute(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                addRecipeInstructionCommand
                        )),
                HttpStatus.CREATED
        );
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    @PatchMapping("/{recipeUuid}/instructions/{recipeInstructionUuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecipeInstructionDto> updateInstruction(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @PathVariable UUID recipeInstructionUuid,
            @RequestBody UpdateRecipeInstructionCommand updateRecipeInstructionCommand
    ) {
        LOGGER.debug("Updating Instruction to {}", recipeUuid);

        return new ResponseEntity<>(
                new RecipeInstructionDto(
                        this.updateRecipeInstructionUseCase.execute(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                recipeInstructionUuid,
                                updateRecipeInstructionCommand
                        )),
                HttpStatus.OK
        );
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    @PatchMapping("/{recipeUuid}/instructions/{recipeInstructionUuid}/position")
    public ResponseEntity<RecipeDto> moveInstruction(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @PathVariable UUID recipeInstructionUuid,
            @RequestBody MoveRecipeInstructionCommand moveRecipeInstructionCommand
    ) {
        LOGGER.debug(
                "moving Instruction {} on {} to {}",
                recipeInstructionUuid,
                recipeUuid,
                moveRecipeInstructionCommand.toPos()
        );

        return new ResponseEntity<>(
                new RecipeDto(
                        this.moveRecipeInstructionUseCase.execute(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                recipeInstructionUuid,
                                moveRecipeInstructionCommand
                        )),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{recipeUuid}/instructions/{recipeInstructionUuid}")
    public ResponseEntity<RecipeDto> deleteInstruction(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable UUID recipeUuid,
            @PathVariable UUID recipeInstructionUuid
    ) {
        LOGGER.debug(
                "deleting Instruction {} on {}",
                recipeInstructionUuid,
                recipeUuid
        );

        return new ResponseEntity<>(
                new RecipeDto(
                        this.deleteRecipeInstructionUseCase.execute(
                                principal.getUser(),
                                new RecipeUUID(recipeUuid),
                                recipeInstructionUuid
                        )),
                HttpStatus.OK
        );
    }
}