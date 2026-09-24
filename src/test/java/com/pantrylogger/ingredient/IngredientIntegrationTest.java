package com.pantrylogger.ingredient;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import com.pantrylogger.domain.IngredientFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.ingredient.Ingredient;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;
import com.pantrylogger.domain.ingredient.create.CreateIngredientCommand;
import com.pantrylogger.domain.ingredient.update.UpdateIngredientCommand;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.restapi.security.CustomUserDetails;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IngredientIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            "postgres:17")
            .withDatabaseName("pantrylogger")
            .withUsername("pantrylogger")
            .withPassword("pantrylogger");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IngredientRepositoryPort ingredientRepository;

    @Autowired
    private UserRepositoryPort userRepository;

    private User testUser;

    private final Ingredient carrot = IngredientFixture.carrot();
    private final Ingredient tomato = IngredientFixture.tomato();

    private final String message = "$.message";
    private final String name = "$.name";
    private final String ingredientsEndPoint = "/ingredients";

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

        ingredientRepository.save(this.carrot);
        ingredientRepository.save(this.tomato);
    }

    private RequestPostProcessor asUser() {
        CustomUserDetails principal = new CustomUserDetails(
                testUser,
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );
        return authentication(auth);
    }

    @Test
    @Sql(statements = "DELETE FROM ingredient_jpa_entity")
    void testGetIngredientsReturns2ingredients() throws Exception {
        mockMvc.perform(get(this.ingredientsEndPoint).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetIngredientReturnsIngredient() throws Exception {
        mockMvc.perform(get(this.ingredientsEndPoint + "/" + this.carrot.getUuid()
                        .uuid()
                        .toString()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.uuid").value(this.carrot.getUuid()
                        .uuid()
                        .toString()))
                .andExpect(jsonPath(this.name).value(this.carrot.getName()));
    }

    @Test
    void testCreateIngredientReturnsCreatedIngredient() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(
                IngredientFixture.created_tomato().getName(),
                IngredientFixture.created_tomato().getDescription()
        );

        mockMvc.perform(post(this.ingredientsEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath(this.name).value(IngredientFixture.created_tomato()
                        .getName()));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("com.pantrylogger.domain.IngredientFixture#badNames")
    void testCreateIngredientWithBadNames(String input) throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(
                input,
                IngredientFixture.created_tomato().getDescription()
        );

        mockMvc.perform(post(this.ingredientsEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 25, 50})
    void testCreateIngredientWithMinValidNameLength(int stringLength) throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(
                "A".repeat(stringLength),
                IngredientFixture.created_tomato().getDescription()
        );

        mockMvc.perform(post(this.ingredientsEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(this.name).value(command.name()));
    }

    @ParameterizedTest
    @NullSource
    void testCreateIngredientWithBadDescription(String input) throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(
                IngredientFixture.created_tomato().getName(),
                input
        );

        mockMvc.perform(post(this.ingredientsEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testCreateIngredientWithBothNameAndDescriptionInvalid() throws Exception {
        CreateIngredientCommand command = new CreateIngredientCommand(
                null,
                null
        );

        mockMvc.perform(post(this.ingredientsEndPoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("com.pantrylogger.domain.IngredientFixture#badNames")
    void testUpdateIngredientBadName(String input) throws Exception {
        UpdateIngredientCommand command = new UpdateIngredientCommand(
                input,
                IngredientFixture.updated_carrot().getDescription()
        );

        mockMvc.perform(patch(this.ingredientsEndPoint + "/" + IngredientFixture.updated_carrot()
                        .getUuid()
                        .uuid()
                        .toString()).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 25, 50})
    void testUpdateIngredientWithValidNameLengths(int stringLength) throws Exception {
        UpdateIngredientCommand command = new UpdateIngredientCommand(
                "A".repeat(stringLength),
                IngredientFixture.updated_carrot().getDescription()
        );

        mockMvc.perform(patch(this.ingredientsEndPoint + "/" + IngredientFixture.updated_carrot()
                        .getUuid()
                        .uuid()
                        .toString()).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(this.name).value(command.name()));
    }

    @ParameterizedTest
    @NullSource
    void testUpdateIngredientWithBadDescription(String input) throws Exception {
        UpdateIngredientCommand command = new UpdateIngredientCommand(
                IngredientFixture.updated_carrot().getName(),
                input
        );

        mockMvc.perform(patch(this.ingredientsEndPoint + "/" + IngredientFixture.updated_carrot()
                        .getUuid()
                        .uuid()
                        .toString()).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateIngredientWithBothNameAndDescriptionInvalid() throws Exception {
        UpdateIngredientCommand command = new UpdateIngredientCommand(
                null,
                null
        );

        mockMvc.perform(patch(this.ingredientsEndPoint + "/" + IngredientFixture.updated_carrot()
                        .getUuid()
                        .uuid()
                        .toString()).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}