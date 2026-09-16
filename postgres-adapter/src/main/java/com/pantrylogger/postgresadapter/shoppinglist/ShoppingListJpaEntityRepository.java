package com.pantrylogger.postgresadapter.shoppinglist;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListJpaEntityRepository extends JpaRepository<ShoppingListJpaEntity, UUID> {
    List<ShoppingListJpaEntity> findByMembersContaining(String member);
}
