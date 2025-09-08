package com.pantrylogger.domain.ingredient;

import java.util.UUID;

public class Ingredient {

    private IngredientUUID uuid;
    private String name;
    private String description;

    public record IngredientUUID(UUID uuid) {
        public IngredientUUID(String strUUID) {
            this(UUID.fromString(strUUID));
        }
    }

    public Ingredient(IngredientUUID uuid, String name, String description) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
    }

    public IngredientUUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingredient)) {
            return false;
        }

        Ingredient that = (Ingredient) o;
        return uuid.equals(that.uuid) &&
                name.equals(that.name) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

}