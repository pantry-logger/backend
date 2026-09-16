package com.pantrylogger.domain.shoppinglist.item.got;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem.ShoppingListItemUUID;
import com.pantrylogger.domain.shoppinglist.ShoppingListItemNotFoundException;
import com.pantrylogger.domain.shoppinglist.ShoppingListNotFoundException;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class MarkItemAsGotOnShoppingListUseCase {
    private final ShoppingListRepositoryPort shoppingListRepository;

    public MarkItemAsGotOnShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingList execute(
            User user,
            ShoppingListUUID shoppingListUUID,
            ShoppingListItemUUID shoppingListItemUUID
    ) {
        ShoppingList shoppingList = this.shoppingListRepository.getByUUID(
                        shoppingListUUID)
                .orElseThrow(() -> new ShoppingListNotFoundException(
                        shoppingListUUID));

        shoppingList.assertIsMember(user.getUsername());

        ShoppingListItem shoppingListItem = shoppingList.getItems()
                .stream()
                .filter(item -> item.getUuid().equals(shoppingListItemUUID))
                .findFirst()
                .orElseThrow(() -> new ShoppingListItemNotFoundException(
                        shoppingListItemUUID));

        shoppingListItem.markAsGot(user.getUsername());

        return this.shoppingListRepository.save(shoppingList);
    }
}
