package com.pantrylogger.restapi.shoppinglist.dto;

import java.time.Instant;
import java.util.UUID;

import com.pantrylogger.domain.shoppinglist.ShoppingListItem;
import com.pantrylogger.restapi.ingredient.IngredientAmountDto;

public record ShoppingListItemDto(
        UUID uuid,
        IngredientAmountDto ingredientAmount,
        String addedBy,
        Instant addedAt,
        GotInfoDto gotInfo
) {
    public ShoppingListItemDto(ShoppingListItem shoppingListItem) {
        this(
                shoppingListItem.getUuid().uuid(),
                new IngredientAmountDto(shoppingListItem.getIngredientAmount()),
                shoppingListItem.getAddedBy().toString(),
                shoppingListItem.getAddedAt(),
                shoppingListItem.getGotInfo().map(GotInfoDto::new).orElse(null)
        );
    }
}
