package com.pantrylogger.domain.shoppinglist.delete;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.shoppinglist.ShoppingListNotFoundException;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class DeleteShoppingListUseCase {
    private final ShoppingListRepositoryPort shoppingListRepository;

    public DeleteShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public void execute(
            User user,
            ShoppingListUUID shoppingListUUID
    ) {
        ShoppingList shoppingList = this.shoppingListRepository.getByUUID(
                        shoppingListUUID)
                .orElseThrow(() -> new ShoppingListNotFoundException(
                        shoppingListUUID));

        shoppingList.assertIsMember(user.getUsername());

        this.shoppingListRepository.delete(shoppingList);
    }
}
