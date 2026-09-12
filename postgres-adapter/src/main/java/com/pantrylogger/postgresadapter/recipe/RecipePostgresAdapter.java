package com.pantrylogger.postgresadapter.recipe;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.recipe.RecipeVisibility;
import com.pantrylogger.domain.user.User;

@Service
public class RecipePostgresAdapter implements RecipeRepositoryPort {

    private final RecipeJpaEntityRepository recipeJpaEntityRepository;

    public RecipePostgresAdapter(RecipeJpaEntityRepository recipeJpaEntityRepository) {
        this.recipeJpaEntityRepository = recipeJpaEntityRepository;
    }

    @Override
    public List<Recipe> getAll() {
        return this.recipeJpaEntityRepository.findAll()
                .stream()
                .map(RecipeJpaEntity::toRecipe)
                .toList();
    }

    @Override
    public List<Recipe> getAllAccessibleBy(User user) {
        return this.recipeJpaEntityRepository.findAllByOwnerUsernameOrVisibility(
                        user.getUsername().toString(),
                        RecipeVisibility.PUBLIC
                )
                .stream()
                .map(RecipeJpaEntity::toRecipe)
                .toList();
    }

    @Override
    public List<Recipe> getAllOwnedBy(User user) {
        return this.recipeJpaEntityRepository.findAllByOwnerUsername(
                        user.getUsername().toString()
                )
                .stream()
                .map(RecipeJpaEntity::toRecipe)
                .toList();
    }

    @Override
    public Optional<Recipe> getByUUID(RecipeUUID uuid) {
        return this.recipeJpaEntityRepository
                .findByIdWithInstructions(uuid.uuid())
                .map(RecipeJpaEntity::toRecipe);

    }

    @Override
    public Recipe save(Recipe recipe) {
        RecipeJpaEntity recipeJpaEntity = new RecipeJpaEntity(recipe);

        return this.recipeJpaEntityRepository.save(recipeJpaEntity).toRecipe();
    }

    @Override
    public void delete(Recipe recipe) {
        try {
            recipeJpaEntityRepository.deleteById(recipe.getUuid().uuid());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(
                    String.format(
                            "Invariant violated: Recipe with UUID %s was expected to exist but was not found",
                            recipe.getUuid().uuid()
                    ),
                    e
            );
        }
    }

}