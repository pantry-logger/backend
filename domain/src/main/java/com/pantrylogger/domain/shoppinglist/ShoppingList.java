package com.pantrylogger.domain.shoppinglist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import jakarta.annotation.Nonnull;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.user.Username;

public class ShoppingList {
    private final ShoppingListUUID uuid;
    private String name;
    private final Set<Username> members;
    private final List<ShoppingListItem> items;

    public record ShoppingListUUID(UUID uuid) {
        public ShoppingListUUID(String strUUID) {
            this(UUID.fromString(strUUID));

        }

        public ShoppingListUUID() {
            this(UUID.randomUUID());
        }

        @Override
        @Nonnull
        public String toString() {
            return this.uuid.toString();
        }
    }

    public ShoppingList(
            ShoppingListUUID uuid,
            String name,
            Set<Username> members,
            List<ShoppingListItem> items
    ) {
        this.uuid = uuid;
        this.name = name;
        this.members = new HashSet<>(members);
        this.items = items;
    }

    public ShoppingList(String name, Set<Username> members) {
        this.uuid = new ShoppingListUUID();
        this.name = name;
        this.members = new HashSet<>(members);
        this.items = new ArrayList<>();
    }

    public ShoppingListUUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Set<Username> getMembers() {
        return members;
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMember(Username username) {
        this.members.add(username);
    }

    public void removeMember(Username username) {
        this.members.remove(username);
    }

    public void assertIsMember(Username username) {
        if (!this.members.contains(username)) {
            throw new ShoppingListNotFoundException(this.uuid);
        }
    }

    public void addItem(Username username, IngredientAmount ingredientAmount) {
        Optional<ShoppingListItem> optionalExistingItem = this.findByIngredient(
                ingredientAmount.getIngredient());
        if (optionalExistingItem.isPresent()) {
            optionalExistingItem.get()
                    .increaseIngredientAmount(ingredientAmount.getAmount());

        } else {
            this.items.add(new ShoppingListItem(
                    ingredientAmount,
                    username
            ));
        }
    }

    private Optional<ShoppingListItem> findByIngredient(Ingredient ingredient) {
        return this.items.stream()
                .filter(item -> item.getIngredientAmount()
                        .getIngredient()
                        .equals(ingredient)).findFirst();
    }

    public void addItems(Username username, List<IngredientAmount> items) {
        items.forEach(item -> this.addItem(username, item));
    }
}
