package com.pantrylogger.postgresadapter.ingredient;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientJpaEntityRepository extends JpaRepository<IngredientJpaEntity, UUID> {

}