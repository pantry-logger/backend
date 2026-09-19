package com.pantrylogger.domain.shoppinglist;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;

public class ShoppingListNotFoundException extends EntityNotFoundException {
    public ShoppingListNotFoundException(ShoppingListUUID shoppingListUUID) {
        super(String.format(
                "Shopping list with Uuid %s not found.",
                shoppingListUUID.toString()
        ));
    }
}
