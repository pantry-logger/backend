package com.pantrylogger.domain.shoppinglist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.ShoppingListFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.user.Username;

class ShoppingListItemsTest {

    private final Username member = UserFixture.basicTestUser().getUsername();
    private final ShoppingList shoppingList = ShoppingListFixture.emptyShoppingList(
            Set.of(
                    this.member));

    @Test
    void addOneItemTest() {
        IngredientAmount ingredientAmount = IngredientFixture.mushroomsAmount();

        assertEquals(0, this.shoppingList.getItems().size());
        this.shoppingList.addItem(this.member, ingredientAmount);
        assertEquals(1, this.shoppingList.getItems().size());
        assertEquals(
                ingredientAmount.getIngredient().getName(),
                this.shoppingList.getItems()
                        .getFirst()
                        .getIngredientAmount()
                        .getIngredient()
                        .getName()
        );
        assertEquals(
                this.member,
                this.shoppingList.getItems().getFirst().getAddedBy()
        );
    }

    @Test
    void addOneItemThatExistsTest() {
        IngredientAmount ingredientAmount = IngredientFixture.mushroomsAmount();
        IngredientAmount ingredientAmountOther = IngredientFixture.mushroomsAmount();
        final int expectedAmountValue = ingredientAmount.getAmount()
                .rawValue() * 2;

        this.shoppingList.addItem(this.member, ingredientAmount);
        assertEquals(
                ingredientAmount.getAmount().rawValue(),
                this.shoppingList.getItems()
                        .getFirst()
                        .getIngredientAmount()
                        .getAmount().rawValue()
        );
        shoppingList.addItem(this.member, ingredientAmountOther);
        assertEquals(1, shoppingList.getItems().size());
        assertEquals(
                expectedAmountValue,
                shoppingList.getItems()
                        .getFirst()
                        .getIngredientAmount()
                        .getAmount().rawValue()
        );
    }

    @Test
    void addItemsTest() {
        Username member = UserFixture.basicTestUser().getUsername();
        ShoppingList shoppingList = ShoppingListFixture.emptyShoppingList(Set.of(
                member));
        List<IngredientAmount> ingredientAmounts = List.of(
                IngredientFixture.mushroomsAmount(),
                IngredientFixture.onionAmount()
        );

        assertEquals(0, shoppingList.getItems().size());
        shoppingList.addItems(member, ingredientAmounts);
        assertEquals(2, shoppingList.getItems().size());
    }
}
