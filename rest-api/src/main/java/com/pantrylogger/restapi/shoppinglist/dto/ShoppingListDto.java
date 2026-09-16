package com.pantrylogger.restapi.shoppinglist.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.user.Username;

public record ShoppingListDto(
        UUID uuid,
        String name,
        Set<String> members,
        List<ShoppingListItemDto> items
) {

    public ShoppingListDto(ShoppingList shoppingList) {
        this(
                shoppingList.getUuid().uuid(),
                shoppingList.getName(),
                shoppingList.getMembers()
                        .stream()
                        .map(Username::toString).collect(Collectors.toSet()),
                shoppingList.getItems()
                        .stream()
                        .map(ShoppingListItemDto::new)
                        .collect(Collectors.toList()
                        )
        );
    }
}
