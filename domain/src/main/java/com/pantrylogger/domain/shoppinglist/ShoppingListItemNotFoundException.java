package com.pantrylogger.domain.shoppinglist;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem.ShoppingListItemUUID;

public class ShoppingListItemNotFoundException extends EntityNotFoundException {
    public ShoppingListItemNotFoundException(ShoppingListItemUUID shoppingListItemUUID) {
        super(String.format(
                "Shopping list item with Uuid %s not found.",
                shoppingListItemUUID.toString()
        ));
    }
}
