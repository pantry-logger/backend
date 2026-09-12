package com.pantrylogger.recipe;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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

import com.pantrylogger.domain.RecipeFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.recipe.Recipe;
import com.pantrylogger.domain.recipe.RecipeRepositoryPort;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.restapi.security.CustomUserDetails;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecipesGetIntegrationTest {

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
    private User anotherTestUser;

    private final Recipe emptyRecipe = RecipeFixture.emptyRecipe();
    private final Recipe privateRecipe = RecipeFixture.privateEmptyRecipe();
    private final Recipe recipeWithInstructions = RecipeFixture.recipeWithInstructions();

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
        this.anotherTestUser = userRepository.save(UserFixture.anotherBasicTestUser());

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

    private RequestPostProcessor asAnotherUser() {
        CustomUserDetails principal = new CustomUserDetails(
                anotherTestUser,
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
    @Sql(statements = {"DELETE FROM recipe_instruction_jpa_entity", "DELETE FROM recipe_jpa_entity"})
    void testGetRecipesReturns2recipes() throws Exception {
        mockMvc.perform(get(this.recipesEndPoint).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetRecipeReturnsRecipe() throws Exception {
        mockMvc.perform(get(this.recipesEndPoint + "/" + this.emptyRecipe.getUuid()
                        .uuid()
                        .toString()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.uuid").value(this.emptyRecipe.getUuid()
                        .uuid()
                        .toString()))
                .andExpect(jsonPath(this.name).value(this.emptyRecipe.getName()));
    }

    @Test
    void testNotFoundIfNotOwnerAndPrivate() throws Exception {

        mockMvc.perform(get(this.recipesEndPoint + "/" + this.privateRecipe
                        .getUuid()
                        .uuid()
                        .toString())
                        .with(asAnotherUser()))
                .andExpect(status().isNotFound());
    }
}