package com.pantrylogger.domain.recipe;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.pantrylogger.domain.exception.EntityMoveOutOfBoundsException;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.amount.Amount;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.ingredient.RecipeIngredientNotFoundException;
import com.pantrylogger.domain.recipe.instruction.RecipeInstructionNotFoundException;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.Username;

public class Recipe {

    private final RecipeUUID uuid;
    private final Username owner;
    private RecipeVisibility visibility;
    private String name;
    private String description;
    private final List<IngredientAmount> ingredients;
    private final List<RecipeInstruction> instructions;

    public record RecipeUUID(UUID uuid) {
        public RecipeUUID(String strUUID) {
            this(UUID.fromString(strUUID));
        }

        public static RecipeUUID generate() {
            return new RecipeUUID(UUID.randomUUID());
        }
    }

    public Recipe(
            RecipeUUID uuid,
            Username owner,
            RecipeVisibility visibility,
            String name,
            String description,
            List<IngredientAmount> ingredients,
            List<RecipeInstruction> instructions
    ) {
        this.uuid = uuid;
        this.owner = owner;
        this.visibility = visibility;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Recipe(
            Username owner,
            RecipeVisibility visibility,
            String name,
            String description,
            List<IngredientAmount> ingredients,
            List<RecipeInstruction> instructions
    ) {
        this.uuid = RecipeUUID.generate();
        this.owner = owner;
        this.visibility = visibility;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public RecipeUUID getUuid() {
        return uuid;
    }

    public Username getOwner() {
        return owner;
    }

    public RecipeVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(RecipeVisibility visibility) {
        this.visibility = visibility;
    }

    public void assertAccessibleBy(User user) {
        if (this.getOwner()
                .equals(user.getUsername()) || this.visibility == RecipeVisibility.LINK || this.visibility == RecipeVisibility.PUBLIC) {
            return;
        }
        throw new RecipeNotFoundException(this.uuid);
    }

    public void assertModifiableBy(User user) {
        if (this.getOwner().equals(user.getUsername())) {
            return;
        }
        if (this.visibility == RecipeVisibility.PRIVATE) {
            throw new RecipeNotFoundException(this.uuid);
        }

        throw new RecipeAccessDeniedException();
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
            throw new EntityMoveOutOfBoundsException(
                    "Invalid position to move to");
        }
        IngredientAmount ingredientToMove = this.ingredients.stream()
                .filter(ia -> ia.getIngredient().uuidEquals(ingredientUUID))
                .findAny()
                .orElseThrow(() -> new RecipeIngredientNotFoundException(
                        this.getUuid(),
                        ingredientUUID
                ));

        this.ingredients.remove(ingredientToMove);
        this.ingredients.add(toPos, ingredientToMove);

    }

    public void updateIngredientAmount(
            IngredientUUID ingredientUUID,
            Amount amount
    ) {
        IngredientAmount ingredientAmount = this.ingredients.stream()
                .filter(ia -> ia.getIngredient().uuidEquals(ingredientUUID))
                .findAny()
                .orElseThrow(() -> new RecipeIngredientNotFoundException(
                        this.getUuid(),
                        ingredientUUID
                ));

        ingredientAmount.setAmount(amount);
    }

    public void deleteIngredient(IngredientUUID ingredientUUID) {
        if (!this.ingredients.removeIf(ia -> ia.getIngredient()
                .uuidEquals(ingredientUUID))) {
            throw new RecipeIngredientNotFoundException(
                    this.getUuid(),
                    ingredientUUID
            );
        }
    }

    public List<RecipeInstruction> getInstructions() {
        return this.instructions;
    }

    public void addInstruction(RecipeInstruction instruction) {
        this.instructions.add(instruction);
    }

    public void moveInstruction(
            RecipeInstructionUUID recipeInstructionUUID,
            int toPos
    ) {
        if (toPos < 0 || toPos >= this.instructions.size()) {
            throw new EntityMoveOutOfBoundsException(
                    "Invalid position to move to");
        }
        RecipeInstruction instructionToMove = this.instructions.stream()
                .filter(instr -> instr.getUuid().equals(recipeInstructionUUID))
                .findAny()
                .orElseThrow(() -> new RecipeInstructionNotFoundException(
                        this.getUuid(),
                        recipeInstructionUUID
                ));

        int currentPos = this.instructions.indexOf(instructionToMove);

        instructions.remove(currentPos);
        instructions.add(toPos, instructionToMove);

    }

    public void deleteInstruction(RecipeInstructionUUID recipeInstructionUUID) {
        if (!instructions.removeIf(instr -> instr.getUuid()
                .equals(recipeInstructionUUID))) {
            throw new RecipeInstructionNotFoundException(
                    this.getUuid(),
                    recipeInstructionUUID
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe that)) {
            return false;
        }

        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid, this.name, this.description);
    }
}