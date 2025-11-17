package com.pantrylogger.postgresadapter.ingredient;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.amount.Amount;
import com.pantrylogger.domain.ingredient.amount.IndividualAmount;
import com.pantrylogger.domain.ingredient.amount.VolumeAmount;
import com.pantrylogger.domain.ingredient.amount.WeightAmount;
import com.pantrylogger.postgresadapter.recipe.RecipeJpaEntity;

@Entity
public class IngredientAmountJpaEntity {

    @EmbeddedId
    private IngredientAmountId id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_id")
    private IngredientJpaEntity ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private RecipeJpaEntity recipe;

    private int amount;

    private AmountType type;

    public IngredientAmountJpaEntity() {
    }

    @SuppressWarnings("PMD.SwitchDensity")
    public IngredientAmountJpaEntity(
            IngredientAmount ingredientAmount,
            RecipeJpaEntity recipeJpaEntity) {
        this.id = new IngredientAmountId(
                recipeJpaEntity.getUuid(),
                ingredientAmount.getIngredient().getUuid().uuid());
        this.ingredient = new IngredientJpaEntity(ingredientAmount.getIngredient());

        switch (ingredientAmount.getAmount()) {
            case Amount.Weight w -> {
                this.amount = w.value().asMilligrams();
                this.type = AmountType.WEIGHT;
            }
            case Amount.Volume v -> {
                this.amount = v.value().asMilliliters();
                this.type = AmountType.VOLUME;
            }
            case Amount.Individual i -> {
                this.amount = i.value().asQuantity();
                this.type = AmountType.INDIVIDUAL;
            }
            default ->
                throw new IllegalArgumentException(
                        "Unknown IngredientAmountEntity subclass: " + ingredientAmount.getAmount());

        }
        this.recipe = recipeJpaEntity;
    }

    public IngredientJpaEntity getIngredient() {
        return ingredient;
    }

    public RecipeJpaEntity getRecipe() {
        return recipe;
    }

    public int getAmount() {
        return amount;
    }

    public AmountType getType() {
        return type;
    }

    public IngredientAmount toIngredientAmount() {
        return switch (this.getType()) {
            case WEIGHT ->
                new IngredientAmount(this.getIngredient().toIngredient(),
                        new Amount.Weight(
                                WeightAmount.fromMilligrams(this.getAmount())));
            case VOLUME ->
                new IngredientAmount(this.getIngredient().toIngredient(),
                        new Amount.Volume(
                                VolumeAmount.fromMilliliters(this.getAmount())));
            case INDIVIDUAL ->
                new IngredientAmount(this.getIngredient().toIngredient(),
                        new Amount.Individual(
                                IndividualAmount.of(this.getAmount())));
            default ->
                throw new IllegalArgumentException(
                        "Unknown IngredientAmountEntity subclass: " + this.getClass());
        };
    }

}