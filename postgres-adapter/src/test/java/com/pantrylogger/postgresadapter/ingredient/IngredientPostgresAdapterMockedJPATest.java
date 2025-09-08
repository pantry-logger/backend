package com.pantrylogger.postgresadapter.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    private IngredientJpaEntityRepository mockRepository = Mockito.mock(IngredientJpaEntityRepository.class);

    private UUID ingredientUUID = UUID.randomUUID();
    private UUID badIngredientUUID = UUID.randomUUID();
    private Ingredient testIngredient;

    @BeforeEach
    void setup() {
        this.adapter = new IngredientPostgresAdapter(this.mockRepository);

        this.testIngredient = new Ingredient(new IngredientUUID(ingredientUUID), "Salt", "Tastes like the sea");
        IngredientJpaEntity testEntity = new IngredientJpaEntity(testIngredient);

        Mockito.when(this.mockRepository.findAll()).thenReturn(List.of(testEntity));
        Mockito.when(this.mockRepository.findById(ingredientUUID)).thenReturn(Optional.of(testEntity));
        Mockito.when(this.mockRepository.findById(badIngredientUUID)).thenReturn(Optional.empty());
        Mockito.when(this.mockRepository.save(Mockito.any(IngredientJpaEntity.class))).thenReturn(testEntity);
    }

    @Test
    void getAllShouldReturnMappedIngredients() {
        List<Ingredient> ingredients = this.adapter.getAll();
        assertEquals(1, ingredients.size());
        assertEquals("Salt", ingredients.get(0).getName());
        assertEquals("Tastes like the sea", ingredients.get(0).getDescription());
    }

    @Test
    void getByUUIDShouldReturnMappedIngredient() {
        Ingredient ingredient = this.adapter.getByUUID(new IngredientUUID(this.ingredientUUID));
        assertEquals("Salt", ingredient.getName());
        assertEquals("Tastes like the sea", ingredient.getDescription());
    }

    @Test
    void getWithBadIdShouldThrowException() {
        assertThrows(
                EntityNotFoundException.class,
                () -> this.adapter.getByUUID(new IngredientUUID(badIngredientUUID)));
    }

    @Test
    void saveShouldReturnSavedIngredient() {
        Ingredient saved = this.adapter.save(this.testIngredient);
        assertEquals("Salt", saved.getName());
        assertEquals("Tastes like the sea", saved.getDescription());
    }

    @Test
    void deleteShouldWorkSuccesfully() {
        this.adapter.delete(new IngredientUUID(ingredientUUID));
        verify(this.mockRepository, times(1)).delete(Mockito.any(IngredientJpaEntity.class));
    }

    @Test
    void deleteWithBadIdShouldThrowException() {
        assertThrows(
                EntityNotFoundException.class,
                () -> this.adapter.delete(new IngredientUUID(badIngredientUUID)));
    }
}