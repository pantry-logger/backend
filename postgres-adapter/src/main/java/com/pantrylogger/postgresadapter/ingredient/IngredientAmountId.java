package com.pantrylogger.postgresadapter.ingredient;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class IngredientAmountId implements Serializable {

    private UUID recipeUuid;
    private UUID ingredientUuid;

    public IngredientAmountId() {
    }

    public IngredientAmountId(UUID recipeUuid, UUID ingredientUuid) {
        this.recipeUuid = recipeUuid;
        this.ingredientUuid = ingredientUuid;
    }

    public UUID getRecipeUuid() {
        return recipeUuid;
    }

    public void getRecipeUuid(UUID recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public UUID getIngredientUuid() {
        return ingredientUuid;
    }

    public void setIngredientUuid(UUID ingredientUuid) {
        this.ingredientUuid = ingredientUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientAmountId)) {
            return false;
        }
        IngredientAmountId that = (IngredientAmountId) o;
        return Objects.equals(recipeUuid, that.recipeUuid) &&
                Objects.equals(ingredientUuid, that.ingredientUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeUuid, ingredientUuid);
    }
}