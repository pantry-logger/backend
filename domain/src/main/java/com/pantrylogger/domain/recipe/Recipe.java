package com.pantrylogger.domain.recipe;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.pantrylogger.domain.exception.EntityMoveOutOfBoundsException;
import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;

public class Recipe {

    private RecipeUUID uuid;
    private String name;
    private String description;
    private List<IngredientAmount> ingredients;
    private List<RecipeInstruction> instructions;

    public record RecipeUUID(UUID uuid) {
        public RecipeUUID(String strUUID) {
            this(UUID.fromString(strUUID));
        }
    }

    public Recipe(
            RecipeUUID uuid,
            String name,
            String description,
            List<IngredientAmount> ingredients,
            List<RecipeInstruction> instructions) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public RecipeUUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IngredientAmount> getIngredients() {
        return this.ingredients;
    }

    public void addIngredient(IngredientAmount ingredient) {
        this.ingredients.add(ingredient);
    }

    public void moveIngredient(IngredientUUID ingredientUUID, int toPos) {
        if (toPos < 0 || toPos >= this.ingredients.size()) {
            throw new EntityMoveOutOfBoundsException("Invalid position to move to");
        }
        IngredientAmount ingredientToMove = this.ingredients.stream()
                .filter(ingredient -> ingredient.getIngredient().getUuid().uuid().equals(ingredientUUID.uuid()))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Recipe Ingredient with UUID %s not found on Recipe %s",
                                ingredientUUID.uuid(), this.getUuid().uuid())));

        int currentPos = this.ingredients.indexOf(ingredientToMove);

        this.ingredients.remove(currentPos);
        this.ingredients.add(toPos, ingredientToMove);

    }

    public void updateIngredientAmount(
            IngredientUUID ingredientUuid,
            int amount,
            IngredientAmountUnit unit) {
        IngredientAmount ingredientAmount = this.ingredients.stream()
                .filter(ia -> ia.getIngredient().getUuid().equals(ingredientUuid))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Recipe Ingredient with UUID %s not found on Recipe %s",
                                ingredientUuid.uuid(), this.getUuid())));

        ingredientAmount.setAmount(amount);
        ingredientAmount.setUnit(unit);
    }

    public void deleteIngredient(IngredientUUID ingredientUuid) {
        if (!this.ingredients.removeIf(ingredient -> ingredient.getIngredient().getUuid().equals(ingredientUuid))) {
            throw new EntityNotFoundException(
                    String.format("Recipe Ingredient with UUID %s not found on Recipe %s",
                            ingredientUuid.uuid(), this.getUuid().uuid()));
        }
    }

    public List<RecipeInstruction> getInstructions() {
        return this.instructions;
    }

    public void addInstruction(RecipeInstruction instruction) {
        this.instructions.add(instruction);
    }

    public void moveInstruction(RecipeInstructionUUID recipeInstructionUuid, int toPos) {
        if (toPos < 0 || toPos >= this.instructions.size()) {
            throw new EntityMoveOutOfBoundsException("Invalid position to move to");
        }
        RecipeInstruction instructionToMove = this.instructions.stream()
                .filter(instr -> instr.getUuid().equals(recipeInstructionUuid))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Recipe Instruction with UUID %s not found on Recipe %s",
                                recipeInstructionUuid, this.getUuid().uuid())));

        int currentPos = this.instructions.indexOf(instructionToMove);

        instructions.remove(currentPos);
        instructions.add(toPos, instructionToMove);

    }

    public void deleteInstruction(RecipeInstructionUUID recipeInstructionUuid) {
        if (!instructions.removeIf(instr -> instr.getUuid().equals(recipeInstructionUuid))) {
            throw new EntityNotFoundException(
                    String.format("Recipe Instruction with UUID %s not found on Recipe %s",
                            recipeInstructionUuid, this.getUuid().uuid()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }

        Recipe that = (Recipe) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid, this.name, this.description);
    }
}