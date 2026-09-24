package com.pantrylogger.recipe;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.recipe.create.CreateRecipeCommand;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.restapi.security.CustomUserDetails;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecipesCreateIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            "postgres:17").withDatabaseName("pantrylogger")
            .withUsername("pantrylogger")
            .withPassword("pantrylogger");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepositoryPort recipeRepository;

    @Autowired
    private UserRepositoryPort userRepository;

    private User testUser;

    private final Recipe emptyRecipe = RecipeFixture.emptyRecipe();
    private final Recipe createRecipe = RecipeFixture.createRecipe();
    private final Recipe recipeWithInstructions = RecipeFixture.recipeWithInstructions();

    private final String message = "$.message";
    private final String name = "$.name";
    private final String recipesEndPoint = "/recipes";

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add(
                "spring.jpa.properties.hibernate.dialect",
                () -> "org.hibernate.dialect.PostgreSQLDialect"
        );
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void setUpTestData() {
        this.testUser = userRepository.save(UserFixture.basicTestUser());

        recipeRepository.save(this.emptyRecipe);
        recipeRepository.save(this.recipeWithInstructions);
    }

    private RequestPostProcessor asUser() {
        CustomUserDetails principal = new CustomUserDetails(
                testUser,
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
        return authentication(auth);
    }

    @Test
    void testCreateRecipeReturnsCreatedRecipe() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                this.createRecipe.getName(),
                this.createRecipe.getDescription(),
                this.createRecipe.getVisibility()
        );

        mockMvc.perform(post(this.recipesEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath(this.name).value(RecipeFixture.createRecipe()
                        .getName()));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("com.pantrylogger.domain.RecipeFixture#badNames")
    void testCreateRecipeWithNullName(String input) throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                input,
                this.createRecipe.getDescription(),
                this.createRecipe.getVisibility()
        );

        mockMvc.perform(post(this.recipesEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 25, 50})
    void testCreateRecipeWithValidNameLength(int stringLength) throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                "a".repeat(stringLength),
                this.createRecipe.getDescription(),
                this.createRecipe.getVisibility()
        );

        mockMvc.perform(post(this.recipesEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value(command.name()));
    }

    @ParameterizedTest
    @NullSource
    void testCreateRecipeWithBadDescription(String input) throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                this.createRecipe.getName(),
                input,
                this.createRecipe.getVisibility()
        );

        mockMvc.perform(post(this.recipesEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithNullVisibility() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(
                this.createRecipe.getName(),
                this.createRecipe.getDescription(),
                null
        );

        mockMvc.perform(post(this.recipesEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateRecipeWithNameDescriptionAndVisibilityInvalid() throws Exception {
        CreateRecipeCommand command = new CreateRecipeCommand(null, null, null);

        mockMvc.perform(post(this.recipesEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}