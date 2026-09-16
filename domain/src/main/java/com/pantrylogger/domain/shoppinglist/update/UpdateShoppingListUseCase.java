package com.pantrylogger.domain.shoppinglist.update;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.shoppinglist.ShoppingListNotFoundException;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
@Validated
public class UpdateShoppingListUseCase {
    private final ShoppingListRepositoryPort shoppingListRepository;

    public UpdateShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingList execute(
            User user,
            ShoppingListUUID shoppingListUUID,
            @Valid UpdateShoppingListCommand updateShoppingListCommand
    ) {
        ShoppingList shoppingList = this.shoppingListRepository.getByUUID(
                        shoppingListUUID)
                .orElseThrow(() -> new ShoppingListNotFoundException(
                        shoppingListUUID));

        shoppingList.assertIsMember(user.getUsername());
        shoppingList.setName(updateShoppingListCommand.name());

        return this.shoppingListRepository.save(shoppingList);
    }
}
