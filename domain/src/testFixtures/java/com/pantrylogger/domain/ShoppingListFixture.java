package com.pantrylogger.domain;

import java.util.Set;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem;
import com.pantrylogger.domain.user.Username;

public class ShoppingListFixture {

    public static ShoppingList emptyShoppingList(Set<Username> members) {
        return new ShoppingList(
                "Test List",
                members
        );
    }

    public static ShoppingListItem aShoppingListItem(Username addedBy) {
        return new ShoppingListItem(
                IngredientFixture.mushroomsAmount(),
                addedBy
        );
    }
}
