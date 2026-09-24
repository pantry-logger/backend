package com.pantrylogger.postgresadapter.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class IngredientPostgresAdapterMockedJPATest {

    private IngredientPostgresAdapter adapter;
    private final IngredientJpaEntityRepository mockRepository = mock(
            IngredientJpaEntityRepository.class);

    private final IngredientUUID ingredientUUID = new IngredientUUID(UUID.randomUUID());
    private final IngredientUUID badIngredientUUID = new IngredientUUID(UUID.randomUUID());
    private Ingredient testIngredient;

    @BeforeEach
    void setup() {
        this.adapter = new IngredientPostgresAdapter(this.mockRepository);

        this.testIngredient = new Ingredient(
                ingredientUUID,
                "Salt",
                "Tastes like the sea"
        );
        IngredientJpaEntity testEntity = new IngredientJpaEntity(testIngredient);

        when(this.mockRepository.findAll())
                .thenReturn(List.of(testEntity));
        when(this.mockRepository.findById(ingredientUUID.uuid()))
                .thenReturn(Optional.of(testEntity));
        when(this.mockRepository.findById(badIngredientUUID.uuid()))
                .thenReturn(Optional.empty());
        when(this.mockRepository.save(Mockito.any(IngredientJpaEntity.class)))
                .thenReturn(testEntity);
    }

    @Test
    void getAllShouldReturnMappedIngredients() {
        List<Ingredient> ingredients = this.adapter.getAll();
        assertEquals(1, ingredients.size());
        assertEquals("Salt", ingredients.get(0).getName());
        assertEquals(
                "Tastes like the sea",
                ingredients.get(0).getDescription()
        );
    }

    @Test
    void getByUUIDShouldReturnIngredient() {
        Optional<Ingredient> optionalIngredient = this.adapter.getByUUID(
                this.ingredientUUID);
        assertTrue(optionalIngredient.isPresent());

        Ingredient ingredient = optionalIngredient.get();

        assertEquals("Salt", ingredient.getName());
        assertEquals("Tastes like the sea", ingredient.getDescription());
    }

    @Test
    void getWithBadIdShouldReturnEmptyOptional() {
        Optional<Ingredient> optionalIngredient = this.adapter.getByUUID(
                this.badIngredientUUID);
        assertTrue(optionalIngredient.isEmpty());
    }

    @Test
    void saveShouldReturnSavedIngredient() {
        Ingredient saved = this.adapter.save(this.testIngredient);
        assertEquals("Salt", saved.getName());
        assertEquals("Tastes like the sea", saved.getDescription());
    }

    @Test
    void deleteShouldWorkSuccesfully() {
        this.adapter.delete(ingredientUUID);
        verify(this.mockRepository, times(1)).delete(Mockito.any(
                IngredientJpaEntity.class));
    }

    @Test
    void deleteWithBadIdShouldThrowException() {
        assertThrows(
                EntityNotFoundException.class,
                () -> this.adapter.delete(this.badIngredientUUID)
        );
    }
}