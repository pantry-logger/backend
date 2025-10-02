package com.pantrylogger.postgresadapter.ingredient;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

@Entity
public class IngredientJpaEntity {

    @Id
    private UUID uuid;
    private String name;
    private String description;

    public IngredientJpaEntity(Ingredient ingredient) {
        this.uuid = ingredient.getUuid().uuid();
        this.name = ingredient.getName();
        this.description = ingredient.getDescription();
    }

    public IngredientJpaEntity() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Ingredient toIngredient() {
        return new Ingredient(
                new IngredientUUID(this.getUuid()),
                this.getName(),
                this.getDescription());
    }
}