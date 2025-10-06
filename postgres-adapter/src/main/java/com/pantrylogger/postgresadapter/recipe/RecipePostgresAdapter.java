package com.pantrylogger.postgresadapter.recipe;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;

@Service
public class RecipePostgresAdapter implements RecipeRepositoryPort {

    private final RecipeJpaEntityRepository recipeJpaEntityRepository;

    public RecipePostgresAdapter(RecipeJpaEntityRepository recipeJpaEntityRepository) {
        this.recipeJpaEntityRepository = recipeJpaEntityRepository;
    }

    @Override
    public List<Recipe> getAll() {
        return this.recipeJpaEntityRepository.findAll().stream().map(RecipeJpaEntity::toRecipe).toList();
    }

    @Override
    public Recipe getByUUID(RecipeUUID uuid) {
        return this.recipeJpaEntityRepository
                .findByIdWithInstructions(uuid.uuid())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Recipe with UUID %s not found", uuid.uuid())))
                .toRecipe();

    }

    @Override
    public Recipe save(Recipe recipe) {
        RecipeJpaEntity recipeJpaEntity = new RecipeJpaEntity(recipe);

        return this.recipeJpaEntityRepository.save(recipeJpaEntity).toRecipe();
    }

    @Override
    public void delete(RecipeUUID uuid) {
        Optional<RecipeJpaEntity> optionalRecipeJpaEntity = recipeJpaEntityRepository
                .findByIdWithInstructions(uuid.uuid());

        if (optionalRecipeJpaEntity.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("Recipe with UUID %s not found", uuid.uuid()));
        }

        RecipeJpaEntity recipeJpaEntity = optionalRecipeJpaEntity.get();

        this.recipeJpaEntityRepository.delete(recipeJpaEntity);
    }

}