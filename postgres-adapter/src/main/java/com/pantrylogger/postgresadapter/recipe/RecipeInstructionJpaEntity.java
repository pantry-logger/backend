package com.pantrylogger.postgresadapter.recipe;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.pantrylogger.domain.recipe.RecipeInstruction;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;

@Entity
public class RecipeInstructionJpaEntity {

    @Id
    private UUID uuid;
    private String instruction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private RecipeJpaEntity recipe;

    public RecipeInstructionJpaEntity() {
    }

    public RecipeInstructionJpaEntity(RecipeInstruction recipeInstruction, RecipeJpaEntity recipe) {
        this.uuid = recipeInstruction.getUuid().uuid();
        this.instruction = recipeInstruction.getInstruction();
        this.recipe = recipe;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public RecipeInstruction toRecipeInstruction() {
        return new RecipeInstruction(
                new RecipeInstructionUUID(this.getUuid()),
                this.getInstruction());
    }
}