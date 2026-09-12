package com.pantrylogger.postgresadapter.recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pantrylogger.domain.recipe.RecipeVisibility;

public interface RecipeJpaEntityRepository extends JpaRepository<RecipeJpaEntity, UUID> {
    @Query("SELECT r FROM RecipeJpaEntity r LEFT JOIN FETCH r.instructions WHERE r.uuid = :id")
    Optional<RecipeJpaEntity> findByIdWithInstructions(@Param("id") UUID uuid);

    List<RecipeJpaEntity> findAllByOwnerUsernameOrVisibility(
            String ownerUsername,
            RecipeVisibility recipeVisibility
    );

    List<RecipeJpaEntity> findAllByOwnerUsername(
            String ownerUsername
    );
}