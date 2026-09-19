package com.pantrylogger.domain.shoppinglist.item.add;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.IngredientNotFoundException;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;
import com.pantrylogger.domain.ingredient.amount.Amount;
import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.shoppinglist.ShoppingListNotFoundException;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class AddItemToShoppingListUseCase {
    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final IngredientRepositoryPort ingredientRepositoryPort;

    public AddItemToShoppingListUseCase(
            ShoppingListRepositoryPort shoppingListRepositoryPort,
            IngredientRepositoryPort ingredientRepositoryPort
    ) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.ingredientRepositoryPort = ingredientRepositoryPort;
    }

    public ShoppingList execute(
            User user,
            ShoppingListUUID shoppingListUUID,
            @Validated
            AddItemToShoppingListCommand addItemToShoppingListCommand
    ) {
        ShoppingList shoppingList = this.shoppingListRepositoryPort.getByUUID(
                        shoppingListUUID)
                .orElseThrow(() -> new ShoppingListNotFoundException(
                        shoppingListUUID));

        shoppingList.assertIsMember(user.getUsername());

        Ingredient ingredient = this.ingredientRepositoryPort.getByUUID(
                        addItemToShoppingListCommand.ingredientUUID())
                .orElseThrow(() -> new IngredientNotFoundException(
                        addItemToShoppingListCommand.ingredientUUID()));

        shoppingList.addItem(
                user.getUsername(), new IngredientAmount(
                        ingredient,
                        Amount.of(
                                addItemToShoppingListCommand.amountValue(),
                                addItemToShoppingListCommand.ingredientAmountUnit()
                        )
                )
        );

        return this.shoppingListRepositoryPort.save(shoppingList);
    }
}
