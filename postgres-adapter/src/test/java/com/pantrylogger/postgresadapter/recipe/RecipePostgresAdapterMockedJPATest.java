package com.pantrylogger.postgresadapter.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.dao.EmptyResultDataAccessException;

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.Recipe.RecipeUUID;

class RecipePostgresAdapterMockedJPATest {
    private RecipePostgresAdapter adapter;
    private RecipeJpaEntityRepository mockRepository = mock(
            RecipeJpaEntityRepository.class);

    private final RecipeUUID badRecipeUUID = RecipeFixture.badUuid();
    private final Recipe missingRecipe = RecipeFixture.missingRecipe();
    private final Recipe testRecipe = RecipeFixture.emptyRecipe();
    private final RecipeJpaEntity testEntity = new RecipeJpaEntity(testRecipe);

    @BeforeEach
    void setup() {
        this.adapter = new RecipePostgresAdapter(this.mockRepository);

        when(this.mockRepository.findAll())
                .thenReturn(List.of(this.testEntity));
        when(this.mockRepository.findByIdWithInstructions(
                testRecipe.getUuid().uuid()))
                .thenReturn(Optional.of(this.testEntity));
        when(this.mockRepository.findByIdWithInstructions(
                badRecipeUUID.uuid())).thenReturn(Optional.empty());
        when(this.mockRepository.save(
                Mockito.any(RecipeJpaEntity.class)))
                .thenReturn(this.testEntity);
    }

    @Test
    void getAllReturnsOneRecipe() {

        var recipes = adapter.getAll();
        assertEquals(
                List.of(this.testRecipe),
                recipes
        );
        assertEquals(testRecipe.getName(), recipes.get(0).getName());
        assertEquals(
                testRecipe.getDescription(),
                recipes.get(0).getDescription()
        );
    }

    @Test
    void getByUUIDShouldReturnMappedRecipe() {
        Optional<Recipe> optionalRecipe = this.adapter.getByUUID(testRecipe.getUuid());
        assertTrue(optionalRecipe.isPresent());
        Recipe recipe = optionalRecipe.get();
        assertEquals(this.testRecipe.getName(), recipe.getName());
        assertEquals(this.testRecipe.getDescription(), recipe.getDescription());
    }

    @Test
    void getWithBadIdShouldReturnOptionalEmpty() {
        Optional<Recipe> optionalRecipe = this.adapter.getByUUID(badRecipeUUID);
        assertTrue(
                optionalRecipe.isEmpty()
        );
    }

    @Test
    void saveShouldReturnSavedRecipe() {
        Recipe saved = this.adapter.save(this.testRecipe);
        assertEquals(this.testRecipe.getName(), saved.getName());
        assertEquals(this.testRecipe.getDescription(), saved.getDescription());
    }

    @Test
    void deleteShouldWorkSuccessfully() {
        this.adapter.delete(testRecipe);
        verify(
                this.mockRepository,
                times(1)
        ).deleteById(Mockito.any(UUID.class));

    }

    @Test
    void deleteWithBadIDShouldThrowException() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(this.mockRepository)
                .deleteById(missingRecipe.getUuid().uuid());

        assertThrows(
                IllegalStateException.class,
                () -> this.adapter.delete(this.missingRecipe)
        );
    }
}