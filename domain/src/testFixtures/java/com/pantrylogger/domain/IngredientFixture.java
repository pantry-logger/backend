package com.pantrylogger.domain;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;

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

    private static final Ingredient BUTTER = new Ingredient(
            new IngredientUUID("2977edae-77ed-4616-a10b-40e69f440828"),
            "Butter",
            "Can't cook without butter!");

    private static final Ingredient ONION = new Ingredient(
            new IngredientUUID("8d60fe14-eaba-4975-bf82-2d73d1c8c197"),
            "Onion",
            "Lots of Layers");

    private static final Ingredient MUSHROOMS = new Ingredient(
            new IngredientUUID("31723fe2-2670-4a72-9c15-315f8b961f72"),
            "Mushrooms",
            "These make the party as they are such Funghis.");

    private static final Ingredient PARMESAN = new Ingredient(
            new IngredientUUID("c06cdc49-d67b-42e9-9de8-d6d3be682314"),
            "Parmesan",
            "Yummy Salty Italian Gold");

    private static final IngredientAmount BUTTER_AMOUNT = new IngredientAmount(
            BUTTER,
            50,
            IngredientAmountUnit.GRAM);
    private static final IngredientAmount ONION_AMOUNT = new IngredientAmount(
            ONION,
            1,
            IngredientAmountUnit.INDIVIDUAL);
    private static final IngredientAmount MUSHROOMS_AMOUNT = new IngredientAmount(
            MUSHROOMS,
            300,
            IngredientAmountUnit.GRAM);

    private static final IngredientAmount PARMESAN_AMOUNT = new IngredientAmount(
            PARMESAN,
            100,
            IngredientAmountUnit.GRAM);

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

    public static Ingredient butter() {
        return BUTTER;
    }

    public static Ingredient onion() {
        return ONION;
    }

    public static Ingredient mushrooms() {
        return MUSHROOMS;
    }

    public static Ingredient parmesan() {
        return PARMESAN;
    }

    public static IngredientAmount butterAmount() {
        return BUTTER_AMOUNT;
    }

    public static IngredientAmount onionAmount() {
        return ONION_AMOUNT;
    }

    public static IngredientAmount mushroomsAmount() {
        return MUSHROOMS_AMOUNT;
    }

    public static IngredientAmount parmesanAmount() {
        return PARMESAN_AMOUNT;
    }
}