package com.pantrylogger.postgresadapter.ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;

class IngredientPostgresAdapterMockedJPATest {

    private IngredientPostgresAdapter adapter;

    private UUID ingredientUUID;
    private Ingredient testIngredient;

    @BeforeEach
    void setup() {
        IngredientJpaEntityRepository mockRepository = Mockito.mock(IngredientJpaEntityRepository.class);
        this.adapter = new IngredientPostgresAdapter(mockRepository);

        this.ingredientUUID = UUID.randomUUID();
        this.testIngredient = new Ingredient(new IngredientUUID(ingredientUUID), "Salt", "Tastes like the sea");
        IngredientJpaEntity testEntity = new IngredientJpaEntity(testIngredient);

        Mockito.when(mockRepository.findAll()).thenReturn(List.of(testEntity));
        Mockito.when(mockRepository.findById(ingredientUUID)).thenReturn(Optional.of(testEntity));
        Mockito.when(mockRepository.save(Mockito.any(IngredientJpaEntity.class))).thenReturn(testEntity);
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
    void saveShouldReturnSavedIngredient() {
        Ingredient saved = this.adapter.save(this.testIngredient);
        assertEquals("Salt", saved.getName());
        assertEquals("Tastes like the sea", saved.getDescription());
    }
}