package com.pantrylogger.domain.ingredient;

public enum IngredientAmountUnit {
    MILLIGRAM("mg"),
    GRAM("g"),
    KILOGRAM("kg"),
    MILLILITER("ml"),
    LITER("l"),
    INDIVIDUAL("qty");

    private final String abbreviation;

    IngredientAmountUnit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}