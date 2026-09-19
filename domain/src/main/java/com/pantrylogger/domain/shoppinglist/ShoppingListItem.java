package com.pantrylogger.domain.shoppinglist;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Nonnull;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.amount.Amount;
import com.pantrylogger.domain.user.Username;

public class ShoppingListItem {

    private final ShoppingListItemUUID uuid;
    private final IngredientAmount ingredientAmount;
    private final Username addedBy;
    private final Instant addedAt;
    private GotInfo gotInfo;

    public record ShoppingListItemUUID(UUID uuid) {
        public ShoppingListItemUUID(String strUUID) {
            this(UUID.fromString(strUUID));

        }

        public ShoppingListItemUUID() {
            this(UUID.randomUUID());
        }

        @Override
        @Nonnull
        public String toString() {
            return this.uuid.toString();
        }
    }

    public record GotInfo(Username gotBy, Instant gotAt) {
        public GotInfo {
            Objects.requireNonNull(gotBy);
            Objects.requireNonNull(gotAt);
        }
    }

    public ShoppingListItem(
            ShoppingListItemUUID uuid,
            IngredientAmount ingredientAmount,
            Username addedBy,
            Instant addedAt,
            GotInfo gotInfo
    ) {
        this.uuid = uuid;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.ingredientAmount = ingredientAmount;
        this.gotInfo = gotInfo;
    }

    public ShoppingListItem(
            IngredientAmount ingredientAmount,
            Username addedBy
    ) {
        this.uuid = new ShoppingListItemUUID();
        this.ingredientAmount = ingredientAmount;
        this.addedBy = addedBy;
        this.addedAt = Instant.now();
    }

    public void markAsGot(Username username) {
        this.gotInfo = new GotInfo(username, Instant.now());
    }

    public void unmarkAsGot() {
        this.gotInfo = null;
    }

    public void increaseIngredientAmount(Amount amount) {
        this.ingredientAmount.increaseAmount(amount);
    }

    public ShoppingListItemUUID getUuid() {
        return this.uuid;
    }

    public IngredientAmount getIngredientAmount() {
        return ingredientAmount;
    }

    public Username getAddedBy() {
        return addedBy;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public Optional<GotInfo> getGotInfo() {
        return Optional.ofNullable(gotInfo);
    }
}
