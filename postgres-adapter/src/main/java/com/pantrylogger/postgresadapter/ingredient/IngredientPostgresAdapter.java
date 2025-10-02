package com.pantrylogger.postgresadapter.ingredient;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;

@Service
public class IngredientPostgresAdapter implements IngredientRepositoryPort {

    private final IngredientJpaEntityRepository ingredientJpaEntityRepository;

    public IngredientPostgresAdapter(IngredientJpaEntityRepository ingredientJpaEntityRepository) {
        this.ingredientJpaEntityRepository = ingredientJpaEntityRepository;
    }

    @Override
    public List<Ingredient> getAll() {
        return ingredientJpaEntityRepository
                .findAll()
                .stream()
                .map(IngredientJpaEntity::toIngredient).toList();
    }

    @Override
    public Ingredient getByUUID(IngredientUUID uuid) {
        return ingredientJpaEntityRepository
                .findById(uuid.uuid())
                .map(IngredientJpaEntity::toIngredient)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Ingredient with UUID %s not found", uuid.uuid())));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        IngredientJpaEntity ingredientJpaEntity = new IngredientJpaEntity(ingredient);

        return this.ingredientJpaEntityRepository.save(ingredientJpaEntity).toIngredient();
    }

    @Override
    public void delete(IngredientUUID uuid) {
        Optional<IngredientJpaEntity> optionalIngredientJpaEntity = ingredientJpaEntityRepository.findById(uuid.uuid());

        if (optionalIngredientJpaEntity.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("Ingredient with UUID %s not found", uuid.uuid()));
        }

        IngredientJpaEntity ingredientJpaEntity = optionalIngredientJpaEntity.get();

        this.ingredientJpaEntityRepository.delete(ingredientJpaEntity);
    }

}