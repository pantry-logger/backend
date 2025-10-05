package com.pantrylogger.postgresadapter.recipe;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.exception.EntityNotFoundException;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;

class RecipePostgresAdapterMockedJPATest {
    private RecipePostgresAdapter adapter;
    private RecipeJpaEntityRepository mockRepository = Mockito.mock(RecipeJpaEntityRepository.class);

    private RecipeUUID badRecipeUUID = RecipeFixture.badUuid();
    private Recipe testRecipe = RecipeFixture.emptyRecipe();
    private RecipeJpaEntity testEntity = new RecipeJpaEntity(testRecipe);

    @BeforeEach
    void setup() {
        this.adapter = new RecipePostgresAdapter(this.mockRepository);

        Mockito.when(this.mockRepository.findAll()).thenReturn(List.of(this.testEntity));
        Mockito.when(this.mockRepository.findByIdWithInstructions(
                testRecipe.getUuid().uuid()))
                .thenReturn(Optional.of(this.testEntity));
        Mockito.when(this.mockRepository.findByIdWithInstructions(
                badRecipeUUID.uuid())).thenReturn(Optional.empty());
        Mockito.when(this.mockRepository.save(
                Mockito.any(RecipeJpaEntity.class))).thenReturn(this.testEntity);
    }

    @Test
    void getAllReturnsOneRecipe() {

        var recipes = adapter.getAll();
        assertEquals(List.of(this.testRecipe),
                recipes);
        assertEquals(testRecipe.getName(), recipes.get(0).getName());
        assertEquals(testRecipe.getDescription(), recipes.get(0).getDescription());
    }

    @Test
    void getByUUIDShouldReturnMappedRecipe() {
        Recipe recipe = this.adapter.getByUUID(testRecipe.getUuid());
        assertEquals(this.testRecipe.getName(), recipe.getName());
        assertEquals(this.testRecipe.getDescription(), recipe.getDescription());
    }

    @Test
    void getWithBadIdShouldThrowException() {
        assertThrows(
                EntityNotFoundException.class,
                () -> this.adapter.getByUUID(badRecipeUUID));
    }

    @Test
    void saveShouldReturnSavedRecipe() {
        Recipe saved = this.adapter.save(this.testRecipe);
        assertEquals(this.testRecipe.getName(), saved.getName());
        assertEquals(this.testRecipe.getDescription(), saved.getDescription());
    }

    @Test
    void deleteShouldWorkSuccesfully() {
        this.adapter.delete(testRecipe.getUuid());
        verify(this.mockRepository, times(1)).delete(Mockito.any(RecipeJpaEntity.class));

    }

    @Test
    void deleteWithBadIDShouldThrowException() {
        assertThrows(
                EntityNotFoundException.class,
                () -> this.adapter.delete(badRecipeUUID));
    }
}