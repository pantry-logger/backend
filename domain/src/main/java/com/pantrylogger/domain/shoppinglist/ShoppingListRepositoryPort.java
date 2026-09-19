package com.pantrylogger.domain.shoppinglist;

import java.util.List;
import java.util.Optional;

import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.user.User;

public interface ShoppingListRepositoryPort {
    Optional<ShoppingList> getByUUID(ShoppingListUUID uuid);

    List<ShoppingList> getAllForUser(User user);

    ShoppingList save(ShoppingList shoppingList);

    void delete(ShoppingList shoppingList);
}
