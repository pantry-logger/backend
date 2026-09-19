package com.pantrylogger.postgresadapter.shoppinglist;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListItemJpaEntityRepository extends JpaRepository<ShoppingListItemJpaEntity, UUID> {
}
