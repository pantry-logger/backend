package com.pantrylogger.restapi.shoppinglist.dto;

import java.time.Instant;

import com.pantrylogger.domain.shoppinglist.ShoppingListItem.GotInfo;

public record GotInfoDto(String gotBy, Instant gotAt) {
    public GotInfoDto(GotInfo gotInfo) {
        this(gotInfo.gotBy().toString(), gotInfo.gotAt());
    }
}
