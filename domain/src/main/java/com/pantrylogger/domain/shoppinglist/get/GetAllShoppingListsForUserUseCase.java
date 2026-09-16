package com.pantrylogger.domain.shoppinglist.get;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class GetAllShoppingListsForUserUseCase {
    private final ShoppingListRepositoryPort shoppingListRepository;

    public GetAllShoppingListsForUserUseCase(ShoppingListRepositoryPort shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public List<ShoppingList> execute(User user) {
        return this.shoppingListRepository.getAllForUser(user);
    }
}
