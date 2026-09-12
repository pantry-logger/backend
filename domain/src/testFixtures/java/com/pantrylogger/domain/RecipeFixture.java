package com.pantrylogger.domain;

import java.util.ArrayList;
import java.util.List;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeInstruction;
import com.pantrylogger.domain.recipe.RecipeInstruction.RecipeInstructionUUID;
import com.pantrylogger.domain.recipe.RecipeVisibility;

public class RecipeFixture {

    private static final Recipe EMPTY_RECIPE = new Recipe(
            new RecipeUUID("63ddc3fb-ee21-4870-ae14-84b4b2d3f9b5"),
            UserFixture.basicTestUser().getUsername(),
            RecipeVisibility.PUBLIC,
            "Pasta Arrabiata",
            "Spicy Tomato Pasta",
            new ArrayList<>(),
            new ArrayList<>()
    );

    private static final Recipe PRIVATE_EMPTY_RECIPE = new Recipe(
            new RecipeUUID("713ee221-64b3-4134-8c7d-f869090b17d4"),
            UserFixture.basicTestUser().getUsername(),
            RecipeVisibility.PRIVATE,
            "Chicken Fried Rice",
            "Leftover rice fried to goodness",
            new ArrayList<>(),
            new ArrayList<>()
    );
    private static final Recipe MISSING_RECIPE = new Recipe(
            new RecipeUUID("d936813c-62c1-4f8e-a57f-9823f57c8483"),
            UserFixture.basicTestUser().getUsername(),
            RecipeVisibility.PUBLIC,
            "Pasta Arrabiata",
            "Spicy Tomato Pasta",
            new ArrayList<>(),
            new ArrayList<>()
    );

    private static final Recipe CREATE_RECIPE = new Recipe(
            new RecipeUUID("de015e14-649d-4af3-a05c-5dd918562fd3"),
            UserFixture.basicTestUser().getUsername(),
            RecipeVisibility.PUBLIC,
            "Thai Red Curry",
            "Yummy Red Coconut curry with veggies and choice of meat",
            new ArrayList<>(),
            new ArrayList<>()
    );

    private static final Recipe RECIPE_WITH_INSTRUCTIONS = new Recipe(
            new RecipeUUID("c728df51-c5f2-44b4-88ec-e9e8488a4b05"),
            UserFixture.basicTestUser().getUsername(),
            RecipeVisibility.PUBLIC,
            "Spaghetti Bolognese",
            "Yummy Spaghetti with meat sauce",
            new ArrayList<>(),
            List.of(
                    new RecipeInstruction(
                            new RecipeInstructionUUID(
                                    "3e533fb9-02ac-4a1c-b936-cbe4dc4ebfcc"),
                            "Grate the Carrots and put them in medium heat with lots of oil in a sauce pan."
                    ),
                    new RecipeInstruction(
                            new RecipeInstructionUUID(
                                    "5e238f9f-f82b-45c0-ab85-ba2723a6407c"),
                            "Chop onions and add to pan. crush garlic into pan. let fry."
                    ),
                    new RecipeInstruction(
                            new RecipeInstructionUUID(
                                    "2ee459a7-f0da-4898-b4f1-f5de6d564774"),
                            "let simmer for awhile until carrot and onion are soft. then add meat and cook until brown."
                    )
            )
    );

    private static final Recipe RECIPE_WITH_INGREDIENTS = new Recipe(
            new RecipeUUID("0b7fed0e-f49a-49cd-b210-38956e618637"),
            UserFixture.basicTestUser().getUsername(),
            RecipeVisibility.PUBLIC,
            "Mushroom Risotto",
            "Creamy Risotto with mushrooms",
            List.of(
                    IngredientFixture.butterAmount(),
                    IngredientFixture.onionAmount(),
                    IngredientFixture.mushroomsAmount()
            ),
            new ArrayList<>()
    );

    private static final RecipeUUID GOOD_UUID = new RecipeUUID(
            "c012d549-692c-4a68-b4cc-a8a04e072e64");

    private static final RecipeUUID BAD_UUID = new RecipeUUID(
            "d936813c-62c1-4f0e-a57f-9823f57c8483");

    private static final RecipeInstruction NEW_INSTRUCTION = new RecipeInstruction(
            new RecipeInstructionUUID("244eef90-3c83-43d8-9963-f95c1090678e"),
            "This is a test instruction."
    );

    public static Recipe emptyRecipe() {
        return EMPTY_RECIPE;
    }

    public static Recipe missingRecipe() {
        return MISSING_RECIPE;
    }

    public static Recipe updatedEmptyRecipe() {

        Recipe updatedRecipe = EMPTY_RECIPE;
        updatedRecipe.setName("Pasta Arrabiata with Aubergine");
        updatedRecipe.setDescription("Spicy Tomato Pasta with Aubergine");

        return updatedRecipe;
    }

    public static Recipe privateEmptyRecipe() {
        return PRIVATE_EMPTY_RECIPE;
    }

    public static Recipe createRecipe() {
        return CREATE_RECIPE;
    }

    public static RecipeUUID goodUuid() {
        return GOOD_UUID;
    }

    public static RecipeUUID badUuid() {
        return BAD_UUID;
    }

    public static Recipe recipeWithInstructions() {
        return RECIPE_WITH_INSTRUCTIONS;
    }

    public static RecipeInstruction newInstruction() {
        return NEW_INSTRUCTION;
    }

    public static Recipe recipeWithIngredients() {
        return RECIPE_WITH_INGREDIENTS;
    }
}