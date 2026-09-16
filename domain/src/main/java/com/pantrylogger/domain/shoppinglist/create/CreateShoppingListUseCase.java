package com.pantrylogger.domain.shoppinglist.create;

import java.util.Set;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
@Validated
public class CreateShoppingListUseCase {
    private final ShoppingListRepositoryPort shoppingListRepository;

    public CreateShoppingListUseCase(ShoppingListRepositoryPort shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingList execute(
            User user,
            @Valid CreateShoppingListCommand createShoppingListCommand
    ) {
        return this.shoppingListRepository.save(
                new ShoppingList(
                        createShoppingListCommand.name(),
                        Set.of(user.getUsername())
                )
        );
    }
}
