package com.pantrylogger.postgresadapter.ingredient;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;
import com.pantrylogger.postgresadapter.recipe.RecipeJpaEntity;

@Entity
public class IngredientAmountJpaEntity {

    @EmbeddedId
    private IngredientAmountId id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_id")
    private IngredientJpaEntity ingredient;
    private int amount;
    private IngredientAmountUnit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private RecipeJpaEntity recipe;

    public IngredientAmountJpaEntity() {
    }

    public IngredientAmountJpaEntity(
            IngredientAmount ingredientAmount,
            RecipeJpaEntity recipe) {
        this.id = new IngredientAmountId(
                recipe.getUuid(),
                ingredientAmount.getIngredient().getUuid().uuid());
        this.ingredient = new IngredientJpaEntity(ingredientAmount.getIngredient());
        this.amount = ingredientAmount.getAmount();
        this.unit = ingredientAmount.getUnit();
        this.recipe = recipe;
    }

    public IngredientJpaEntity getIngredient() {
        return ingredient;
    }

    public int getAmount() {
        return amount;
    }

    public IngredientAmountUnit getUnit() {
        return unit;
    }

    public RecipeJpaEntity getRecipe() {
        return recipe;
    }

    public IngredientAmount toIngredientAmount() {
        return new IngredientAmount(
                this.ingredient.toIngredient(),
                this.amount,
                this.unit);

    }

}