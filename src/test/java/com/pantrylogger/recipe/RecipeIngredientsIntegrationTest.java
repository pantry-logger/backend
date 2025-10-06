package com.pantrylogger.recipe;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientAmountUnit;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.recipe.ingredient.add.AddIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.move.MoveIngredientAmountCommand;
import com.pantrylogger.domain.recipe.ingredient.update.UpdateIngredientAmountCommand;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class RecipeIngredientsIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("pantrylogger")
            .withUsername("pantrylogger")
            .withPassword("pantrylogger");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepositoryPort recipeRepository;
    @Autowired
    private IngredientRepositoryPort ingredientRepository;

    private Recipe emptyRecipe = RecipeFixture.emptyRecipe();
    private Recipe recipeWithIngredients = RecipeFixture.recipeWithIngredients();

    private String message = "$.message";
    private String recipesEndPoint = "/recipes";
    private String ingredientsEndPoint = "/ingredients";
    private String positionEndpoint = "/position";

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void setUpTestData() {
        ingredientRepository.save(IngredientFixture.butter());
        ingredientRepository.save(IngredientFixture.onion());
        ingredientRepository.save(IngredientFixture.mushrooms());
        ingredientRepository.save(IngredientFixture.parmesan());
        recipeRepository.save(this.emptyRecipe);
        recipeRepository.save(this.recipeWithIngredients);
    }

    @Test
    void testAddIngredientAmountShouldAddIngredient() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddIngredientAmountCommand command = new AddIngredientAmountCommand(
                IngredientFixture.parmesan().getUuid(),
                100,
                IngredientAmountUnit.GRAM);

        mockMvc.perform(post(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ingredient").exists());

        mockMvc.perform(get(this.recipesEndPoint + "/" + recipeUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients.length()").value(1));
    }

    @Test
    void testAddRecipeIngredientWithNullsAnd0() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddIngredientAmountCommand command = new AddIngredientAmountCommand(
                null, 0, null);

        mockMvc.perform(post(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testAddRecipeIngredientWithNullIngredient() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddIngredientAmountCommand command = new AddIngredientAmountCommand(
                null, 500, IngredientAmountUnit.GRAM);

        mockMvc.perform(post(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testAddRecipeIngredientWithNullUnit() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddIngredientAmountCommand command = new AddIngredientAmountCommand(
                IngredientFixture.parmesan().getUuid(), 500, null);

        mockMvc.perform(post(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateIngredientAmountShouldAddIngredient() throws Exception {
        UUID recipeUuid = recipeWithIngredients.getUuid().uuid();
        UUID ingredientUuid = recipeWithIngredients.getIngredients().get(0).getIngredient().getUuid().uuid();

        UpdateIngredientAmountCommand command = new UpdateIngredientAmountCommand(
                1,
                IngredientAmountUnit.KILOGRAM);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint + "/" + ingredientUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredient").exists())
                .andExpect(jsonPath("$.amount").value(1))
                .andExpect(jsonPath("$.unit").value("KILOGRAM"));
    }

    @Test
    void testUpdateRecipeIngredientWithNullsAnd0() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();
        UUID ingredientUuid = recipeWithIngredients.getIngredients().get(0).getIngredient().getUuid().uuid();

        UpdateIngredientAmountCommand command = new UpdateIngredientAmountCommand(
                0,
                null);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint + "/" + ingredientUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeIngredientWithNullUnit() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();
        UUID ingredientUuid = recipeWithIngredients.getIngredients().get(0).getIngredient().getUuid().uuid();

        UpdateIngredientAmountCommand command = new UpdateIngredientAmountCommand(
                1,
                null);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint + "/" + ingredientUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeIngredientWith0Amount() throws Exception {
        this.recipeWithIngredients = RecipeFixture.recipeWithIngredients();
        recipeRepository.save(this.recipeWithIngredients);

        UUID recipeUuid = emptyRecipe.getUuid().uuid();
        UUID ingredientUuid = recipeWithIngredients.getIngredients().get(0).getIngredient().getUuid().uuid();

        UpdateIngredientAmountCommand command = new UpdateIngredientAmountCommand(
                0,
                IngredientAmountUnit.GRAM);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint + "/" + ingredientUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeIngredientWithBadIngredientUuid() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();
        UUID badIngredientUuid = IngredientFixture.badUUID().uuid();

        AddIngredientAmountCommand command = new AddIngredientAmountCommand(
                null, 500, IngredientAmountUnit.GRAM);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + recipeUuid + this.ingredientsEndPoint + "/" + badIngredientUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testMoveRecipeIngredientShouldMoveIngredientUp() throws Exception {
        this.recipeWithIngredients = RecipeFixture.recipeWithIngredients();
        recipeRepository.save(this.recipeWithIngredients);

        int fromPos = 0;
        int toPos = 2;
        Ingredient ingredient = this.recipeWithIngredients.getIngredients().get(fromPos).getIngredient();

        MoveIngredientAmountCommand command = new MoveIngredientAmountCommand(toPos);

        mockMvc.perform(patch(
                this.recipesEndPoint + "/" +
                        this.recipeWithIngredients.getUuid().uuid().toString() + this.ingredientsEndPoint + "/" +
                        ingredient.getUuid().uuid().toString() + this.positionEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients").exists());

        mockMvc
                .perform(get(this.recipesEndPoint + "/" + this.recipeWithIngredients.getUuid().uuid().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients[" + toPos + "].ingredient.uuid")
                        .value(ingredient.getUuid().uuid().toString()));

    }

    @Test
    void testMoveRecipeIngredientShouldMoveIngredientDown() throws Exception {
        this.recipeWithIngredients = RecipeFixture.recipeWithIngredients();
        recipeRepository.save(this.recipeWithIngredients);

        int fromPos = 2;
        int toPos = 0;
        Ingredient ingredient = this.recipeWithIngredients.getIngredients().get(fromPos).getIngredient();

        MoveIngredientAmountCommand command = new MoveIngredientAmountCommand(toPos);

        mockMvc.perform(patch(
                this.recipesEndPoint + "/" +
                        this.recipeWithIngredients.getUuid().uuid().toString() + this.ingredientsEndPoint + "/" +
                        ingredient.getUuid().uuid().toString() + this.positionEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients").exists());

        mockMvc
                .perform(get(this.recipesEndPoint + "/" + this.recipeWithIngredients.getUuid().uuid().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients[" + toPos + "].ingredient.uuid")
                        .value(ingredient.getUuid().uuid().toString()));

    }

    @Test
    void testMoveRecipeIngredientShouldFailWithOutOfBoundsBelow() throws Exception {
        int fromPos = 0;
        int toPos = -1;
        Ingredient ingredient = this.recipeWithIngredients.getIngredients().get(fromPos).getIngredient();

        MoveIngredientAmountCommand command = new MoveIngredientAmountCommand(toPos);

        mockMvc.perform(patch(
                this.recipesEndPoint + "/" +
                        this.recipeWithIngredients.getUuid().uuid().toString() + this.ingredientsEndPoint + "/" +
                        ingredient.getUuid().uuid().toString() + this.positionEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());

    }

    @Test
    void testMoveRecipeIngredientShouldFailWithOutOfBoundsAbove() throws Exception {
        int fromPos = 0;
        int toPos = 10;
        Ingredient ingredient = this.recipeWithIngredients.getIngredients().get(fromPos).getIngredient();

        MoveIngredientAmountCommand command = new MoveIngredientAmountCommand(toPos);

        mockMvc.perform(patch(
                this.recipesEndPoint + "/" +
                        this.recipeWithIngredients.getUuid().uuid().toString() + this.ingredientsEndPoint + "/" +
                        ingredient.getUuid().uuid().toString() + this.positionEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());

    }

    @Test
    void testMoveRecipeIngredientShouldFailIngredientNotFound() throws Exception {
        int toPos = 1;

        IngredientUUID badIngredientUuid = IngredientFixture.badUUID();

        MoveIngredientAmountCommand command = new MoveIngredientAmountCommand(toPos);

        mockMvc.perform(patch(
                this.recipesEndPoint + "/" +
                        this.recipeWithIngredients.getUuid().uuid().toString() + this.ingredientsEndPoint + "/" +
                        badIngredientUuid.uuid().toString() + this.positionEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testDeleteRecipeIngredientShouldDeleteIngredient() throws Exception {

        Ingredient ingredient = this.recipeWithIngredients.getIngredients().get(1).getIngredient();
        mockMvc.perform(delete(
                this.recipesEndPoint + "/" +
                        this.recipeWithIngredients.getUuid().uuid().toString() + this.ingredientsEndPoint + "/" +
                        ingredient.getUuid().uuid().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients").exists());

        mockMvc.perform(get(this.recipesEndPoint + "/" + this.recipeWithIngredients.getUuid().uuid().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients.length()").value(2));
    }

    @Test
    void testDeleteRecipeIngredientShouldFailIngredientNotFound() throws Exception {
        IngredientUUID badIngredientUuid = IngredientFixture.badUUID();

        mockMvc.perform(delete(
                this.recipesEndPoint + "/" +
                        this.recipeWithIngredients.getUuid().uuid().toString() + this.ingredientsEndPoint + "/" +
                        badIngredientUuid.uuid().toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(this.message).exists());

    }
}