package com.pantrylogger.postgresadapter.shoppinglist;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.pantrylogger.domain.ingredient.IngredientAmount;
import com.pantrylogger.domain.ingredient.amount.Amount;
import com.pantrylogger.domain.ingredient.amount.IndividualAmount;
import com.pantrylogger.domain.ingredient.amount.VolumeAmount;
import com.pantrylogger.domain.ingredient.amount.WeightAmount;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem.GotInfo;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem.ShoppingListItemUUID;
import com.pantrylogger.domain.user.Username;
import com.pantrylogger.postgresadapter.ingredient.AmountType;
import com.pantrylogger.postgresadapter.ingredient.IngredientJpaEntity;

@Entity
@Table(name = "shopping_list_items")
public class ShoppingListItemJpaEntity {

    @Id
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopping_list_uuid", nullable = false)
    private ShoppingListJpaEntity shoppingList;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ingredient_uuid", nullable = false)
    private IngredientJpaEntity ingredient;

    @Column(nullable = false)
    private int amountValue;

    @Enumerated(EnumType.STRING)
    private AmountType amountType;

    @Column(nullable = false)
    private String addedBy;

    @Column(nullable = false)
    private Instant addedAt;

    private String gotBy;

    private Instant gotAt;

    public ShoppingListItemJpaEntity() {
    }

    public ShoppingListItemJpaEntity(
            UUID uuid,
            ShoppingListJpaEntity shoppingListJpaEntity,
            IngredientJpaEntity ingredientJpaEntity,
            int amountValue,
            AmountType amountType,
            String addedBy,
            Instant addedAt,
            Optional<GotInfo> gotInfo
    ) {
        this.uuid = uuid;
        this.shoppingList = shoppingListJpaEntity;
        this.ingredient = ingredientJpaEntity;
        this.amountValue = amountValue;
        this.amountType = amountType;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        gotInfo.ifPresentOrElse(
                gI -> {
                    this.gotBy = gI.gotBy().toString();
                    this.gotAt = gI.gotAt();
                },
                () -> {
                    this.gotBy = null;
                    this.gotAt = null;
                }
        );
    }

    public ShoppingListItemJpaEntity(
            ShoppingListJpaEntity shoppingListJpaEntity,
            ShoppingListItem shoppingListItem
    ) {
        this(
                shoppingListItem.getUuid().uuid(),
                shoppingListJpaEntity,
                new IngredientJpaEntity(shoppingListItem.getIngredientAmount()
                        .getIngredient()),
                extractAmountValue(shoppingListItem),
                extractAmountType(shoppingListItem),
                shoppingListItem.getAddedBy().toString(),
                shoppingListItem.getAddedAt(),
                shoppingListItem.getGotInfo()
        );
    }

    private static int extractAmountValue(ShoppingListItem shoppingListItem) {
        return switch (shoppingListItem.getIngredientAmount().getAmount()) {
            case Amount.Weight(WeightAmount value) -> value.asMilligrams();
            case Amount.Volume v -> v.value().asMilliliters();
            case Amount.Individual i -> i.value().asQuantity();
        };
    }

    private static AmountType extractAmountType(ShoppingListItem shoppingListItem) {
        return switch (shoppingListItem.getIngredientAmount().getAmount()) {
            case Amount.Weight _ -> AmountType.WEIGHT;
            case Amount.Volume _ -> AmountType.VOLUME;
            case Amount.Individual _ -> AmountType.INDIVIDUAL;
        };
    }

    private IngredientAmount getIngredientAmount() {
        return switch (this.amountType) {
            case WEIGHT -> new IngredientAmount(
                    this.ingredient.toIngredient(),
                    new Amount.Weight(WeightAmount.fromMilligrams(this.amountValue))
            );
            case VOLUME -> new IngredientAmount(
                    this.ingredient.toIngredient(),
                    new Amount.Volume(VolumeAmount.fromMilliliters(this.amountValue))
            );
            case INDIVIDUAL -> new IngredientAmount(
                    this.ingredient.toIngredient(),
                    new Amount.Individual(IndividualAmount.of(this.amountValue))
            );
        };
    }

    private GotInfo getGotInfo() {
        if (this.gotBy == null) {
            return null;
        }

        return new GotInfo(new Username(this.gotBy), this.gotAt);
    }

    public UUID getUuid() {
        return uuid;
    }

    public ShoppingListItem toShoppingListItem() {

        return new ShoppingListItem(
                new ShoppingListItemUUID(this.uuid),
                this.getIngredientAmount(),
                new Username(this.addedBy),
                this.addedAt,
                this.getGotInfo()
        );
    }
}
