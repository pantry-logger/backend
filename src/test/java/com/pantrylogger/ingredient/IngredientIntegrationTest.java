package com.pantrylogger.ingredient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.pantrylogger.domain.ingredient.CreateIngredientCommand;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.Ingredient.IngredientUUID;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class IngredientIntegrationTest {

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
    private IngredientRepositoryPort ingredientRepository;

    private String ingredientUUID1 = "11111111-1111-1111-1111-111111111111";
    private String ingredientUUID2 = "22222222-2222-2222-2222-222222222222";

    private String message = "$.message";
    private String name = "$.name";
    private String createdIngredientName = "Tomato";
    private String description = "$.description";
    private String createdIngredientDescription = "Some description";
    private String ingredientsEndPoint = "/ingredients";

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
        ingredientRepository.save(
                new Ingredient(
                        new IngredientUUID(this.ingredientUUID1),
                        "Carrot",
                        "Crunchy orange stick"));
        ingredientRepository.save(
                new Ingredient(
                        new IngredientUUID(this.ingredientUUID2),
                        "Salt",
                        "Tastes like the sea"));
    }

    @Test
    @Sql(statements = "DELETE FROM ingredient_jpa_entity")
    void testGetIngredientsReturns2ingredients() throws Exception {
        mockMvc.perform(get(this.ingredientsEndPoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetIngredientReturnsIngredient() throws Exception {
        mockMvc.perform(get(this.ingredientsEndPoint + "/" + this.ingredientUUID1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.uuid").value(ingredientUUID1))
                .andExpect(jsonPath(this.name).value("Carrot"));
    }

    @Test
    void testCreateIngredientReturnsCreatedIngredient() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(this.createdIngredientName, "Red and Juicy");

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath(this.name).value(this.createdIngredientName));
    }

    @Test
    void testCreateIngredientWithValidCommand() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(this.createdIngredientName, "Red and juicy");

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value(this.createdIngredientName))
                .andExpect(jsonPath(this.description).value("Red and juicy"));
    }

    @Test
    void testCreateIngredientWithNullName() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(null, this.createdIngredientDescription);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateIngredientWithBlankName() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand("", this.createdIngredientDescription);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateIngredientWithWhitespaceOnlyName() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand("   ", this.createdIngredientDescription);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateIngredientWithNameTooShort() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand("A", this.createdIngredientDescription);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateIngredientWithNameTooLong() throws Exception {
        String longName = "A".repeat(51); // 51 characters
        CreateIngredientCommand command = new CreateIngredientCommand(longName, this.createdIngredientDescription);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateIngredientWithMinValidNameLength() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand("AB", this.createdIngredientDescription);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value("AB"));
    }

    @Test
    void testCreateIngredientWithMaxValidNameLength() throws Exception {
        String maxName = "A".repeat(50); // 50 characters
        CreateIngredientCommand command = new CreateIngredientCommand(maxName, this.createdIngredientDescription);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value(maxName));
    }

    @Test
    void testCreateIngredientWithNullDescription() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(this.createdIngredientName, null);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateIngredientWithEmptyDescription() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(this.createdIngredientName, "");

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value(this.createdIngredientName))
                .andExpect(jsonPath(this.description).value(""));
    }

    @Test
    void testCreateIngredientWithBothNameAndDescriptionInvalid() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(null, null);

        mockMvc.perform(post(this.ingredientsEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}