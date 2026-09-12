package com.pantrylogger.postgresadapter.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeVisibility;
import com.pantrylogger.domain.user.Username;
import com.pantrylogger.postgresadapter.ingredient.IngredientAmountJpaEntity;

@Entity
public class RecipeJpaEntity {

    @Id
    private UUID uuid;

    private String ownerUsername;

    @Enumerated(EnumType.STRING)
    private RecipeVisibility visibility;

    private String name;
    private String description;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    private List<IngredientAmountJpaEntity> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    private List<RecipeInstructionJpaEntity> instructions = new ArrayList<>();

    public RecipeJpaEntity() {
    }

    public RecipeJpaEntity(Recipe recipe) {
        this.uuid = recipe.getUuid().uuid();
        this.ownerUsername = recipe.getOwner().toString();
        this.visibility = recipe.getVisibility();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.ingredients = recipe.getIngredients()
                .stream()
                .map(ia -> new IngredientAmountJpaEntity(ia, this))
                .collect(Collectors.toCollection(ArrayList::new));
        this.instructions = recipe.getInstructions()
                .stream()
                .map(instr -> new RecipeInstructionJpaEntity(instr, this))
                .collect(Collectors.toCollection(ArrayList::new));
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

    public List<RecipeInstructionJpaEntity> getInstructions() {
        return instructions;
    }

    public Recipe toRecipe() {
        return new Recipe(
                new RecipeUUID(this.uuid),
                new Username(this.ownerUsername),
                this.visibility,
                this.name,
                this.description,
                this.ingredients.stream()
                        .map(IngredientAmountJpaEntity::toIngredientAmount)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.instructions.stream()
                        .map(RecipeInstructionJpaEntity::toRecipeInstruction)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

}