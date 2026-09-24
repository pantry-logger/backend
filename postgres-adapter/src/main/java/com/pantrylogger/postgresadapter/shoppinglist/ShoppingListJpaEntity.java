package com.pantrylogger.postgresadapter.shoppinglist;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.user.Username;

@Entity
@Table(name = "shopping_lists")
public class ShoppingListJpaEntity {

    @Id
    private UUID uuid;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "shopping_list_members",
            joinColumns = @JoinColumn(name = "shopping_list_uuid")
    )
    @Column(name = "username")
    private Set<String> members;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShoppingListItemJpaEntity> items;

    public ShoppingListJpaEntity() {
    }

    public ShoppingListJpaEntity(ShoppingList shoppingList) {
        this.uuid = shoppingList.getUuid().uuid();
        this.name = shoppingList.getName();
        this.members = shoppingList.getMembers()
                .stream()
                .map(Username::toString)
                .collect(
                        Collectors.toSet());

        this.items = shoppingList.getItems()
                .stream()
                .map(item -> new ShoppingListItemJpaEntity(this, item))
                .toList();
    }

    public List<ShoppingListItemJpaEntity> getItems() {
        return items;
    }

    public ShoppingList toShoppingList() {
        return new ShoppingList(
                new ShoppingListUUID(this.uuid),
                this.name,
                this.members.stream()
                        .map(Username::new)
                        .collect(Collectors.toSet()),
                this.items.stream()
                        .map(ShoppingListItemJpaEntity::toShoppingListItem)
                        .toList()

        );
    }

}
