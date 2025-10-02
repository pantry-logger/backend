package com.pantrylogger.domain;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

public class IngredientFixture {

    private static final Ingredient CARROT = new Ingredient(
            new IngredientUUID("2d3d69d2-9169-4df4-b1d4-580e30e792a9"),
            "Carrot",
            "Crunchy orange stick");

    private static final Ingredient UPDATED_CARROT = new Ingredient(
            new IngredientUUID("2d3d69d2-9169-4df4-b1d4-580e30e792a9"),
            "Baby Carrot",
            "Still Crunchy but now small");

    private static final Ingredient TOMATO = new Ingredient(
            new IngredientUUID("83b9ac20-89c2-4a2b-8da1-94fdf8aa7057"),
            "Tomato",
            "Luscious ripe red orb");

    private static final Ingredient CREATED_TOMATO = new Ingredient(
            new IngredientUUID("4b03040d-5b9b-41d6-b5f0-7022265d013b"),
            "Cherry Tomato",
            "Little luscious red orbs");

    private static final IngredientUUID GOOD_UUID = new IngredientUUID("a2bb99bf-c021-4005-b387-21c4df774568");

    private static final IngredientUUID BAD_UUID = new IngredientUUID("3146cc20-3461-41be-8ae0-b3dc3aea47c3");

    public static Ingredient carrot() {
        return CARROT;
    }

    public static Ingredient updated_carrot() {
        return UPDATED_CARROT;
    }

    public static Ingredient tomato() {
        return TOMATO;
    }

    public static Ingredient created_tomato() {
        return CREATED_TOMATO;
    }

    public static IngredientUUID badUUID() {
        return BAD_UUID;
    }

    public static IngredientUUID goodUUID() {
        return GOOD_UUID;
    }
}