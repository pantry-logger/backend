package com.pantrylogger.recipe;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.recipe.create.CreateRecipeCommand;
import com.pantrylogger.domain.recipe.update.UpdateRecipeCommand;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class RecipesIntegrationTest {

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

    private Recipe emptyRecipe = RecipeFixture.emptyRecipe();
    private Recipe recipeWithInstructions = RecipeFixture.recipeWithInstructions();

    private String message = "$.message";
    private String name = "$.name";
    private String description = "$.description";
    private String recipesEndPoint = "/recipes";

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
        recipeRepository.save(this.emptyRecipe);
        recipeRepository.save(this.recipeWithInstructions);
    }

    @Test
    @Sql(statements = {
            "DELETE FROM recipe_instruction_jpa_entity",
            "DELETE FROM recipe_jpa_entity"
    })
    void testGetRecipesReturns2recipes() throws Exception {
        mockMvc.perform(get(this.recipesEndPoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetRecipeReturnsRecipe() throws Exception {
        mockMvc.perform(get(this.recipesEndPoint + "/" + this.emptyRecipe.getUuid().uuid().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.uuid").value(this.emptyRecipe.getUuid().uuid().toString()))
                .andExpect(jsonPath(this.name).value(this.emptyRecipe.getName()));
    }

    @Test
    void testCreateRecipeReturnsCreatedRecipe() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                RecipeFixture.createRecipe().getName(),
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath(this.name).value(RecipeFixture.createRecipe().getName()));
    }

    @Test
    void testCreateRecipeWithNullName() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(null,
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithBlankName() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand("",
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithWhitespaceOnlyName() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand("   ",
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithNameTooShort() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand("A",
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithNameTooLong() throws Exception {
        String longName = "A".repeat(51); // 51 characters
        CreateRecipeCommand command = new CreateRecipeCommand(longName,
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithMinValidNameLength() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand("Curry",
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value("Curry"));
    }

    @Test
    void testCreateRecipeWithMaxValidNameLength() throws Exception {
        String maxName = "A".repeat(50); // 50 characters
        CreateRecipeCommand command = new CreateRecipeCommand(maxName,
                RecipeFixture.createRecipe().getDescription());

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value(maxName));
    }

    @Test
    void testCreateRecipeWithNullDescription() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                RecipeFixture.createRecipe().getName(),
                null);

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithEmptyDescription() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                RecipeFixture.createRecipe().getName(),
                "");

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value(RecipeFixture.createRecipe().getName()))
                .andExpect(jsonPath(this.description).value(""));
    }

    @Test
    void testCreateRecipeWithBothNameAndDescriptionInvalid() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(null, null);

        mockMvc.perform(post(this.recipesEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testUpdateRecipeWithNullName() throws Exception {
        UpdateRecipeCommand command = new UpdateRecipeCommand(null,
                RecipeFixture.updatedEmptyRecipe().getDescription());

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeWithBlankName() throws Exception {
        UpdateRecipeCommand command = new UpdateRecipeCommand("",
                RecipeFixture.updatedEmptyRecipe().getDescription());

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeWithWhitespaceOnlyName() throws Exception {
        UpdateRecipeCommand command = new UpdateRecipeCommand("   ",
                RecipeFixture.updatedEmptyRecipe().getDescription());

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeWithNameTooShort() throws Exception {
        UpdateRecipeCommand command = new UpdateRecipeCommand("A",
                RecipeFixture.updatedEmptyRecipe().getDescription());

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeWithNameTooLong() throws Exception {
        String longName = "A".repeat(51); // 51 characters
        UpdateRecipeCommand command = new UpdateRecipeCommand(longName,
                RecipeFixture.updatedEmptyRecipe().getDescription());

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeWithMinValidNameLength() throws Exception {
        UpdateRecipeCommand command = new UpdateRecipeCommand("Curry",
                RecipeFixture.updatedEmptyRecipe().getDescription());

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(this.name).value("Curry"));
    }

    @Test
    void testUpdateRecipeWithMaxValidNameLength() throws Exception {
        String maxName = "A".repeat(50);
        UpdateRecipeCommand command = new UpdateRecipeCommand(maxName,
                RecipeFixture.updatedEmptyRecipe().getDescription());

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(this.name).value(maxName));
    }

    @Test
    void testUpdateRecipeWithNullDescription() throws Exception {
        UpdateRecipeCommand command = new UpdateRecipeCommand(
                RecipeFixture.updatedEmptyRecipe().getName(),
                null);

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeWithEmptyDescription() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                RecipeFixture.updatedEmptyRecipe().getName(),
                "");

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(this.name).value(RecipeFixture.updatedEmptyRecipe().getName()))
                .andExpect(jsonPath(this.description).value(""));
    }

    @Test
    void testUpdateRecipeWithBothNameAndDescriptionInvalid() throws Exception {
        UpdateRecipeCommand command = new UpdateRecipeCommand(null, null);

        mockMvc.perform(
                patch(this.recipesEndPoint + "/" + RecipeFixture.updatedEmptyRecipe().getUuid().uuid().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}