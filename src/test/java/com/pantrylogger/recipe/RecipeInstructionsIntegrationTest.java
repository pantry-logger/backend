package com.pantrylogger.recipe;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.pantrylogger.domain.recipe.instruction.add.AddRecipeInstructionCommand;
import com.pantrylogger.domain.recipe.instruction.move.MoveRecipeInstructionCommand;
import com.pantrylogger.domain.recipe.instruction.update.UpdateRecipeInstructionCommand;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.restapi.security.CustomUserDetails;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecipeInstructionsIntegrationTest {

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

    private Recipe emptyRecipe = RecipeFixture.emptyRecipe();
    private Recipe recipeWithInstructions = RecipeFixture.recipeWithInstructions();

    private String message = "$.message";
    private String recipesEndPoint = "/recipes";
    private String instructionsEndPoint = "/instructions";
    private String positionEndpoint = "/position";

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
    void testAddRecipeInstructionShouldAddInstruction() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddRecipeInstructionCommand command = new AddRecipeInstructionCommand(
                RecipeFixture.newInstruction().getInstruction());

        mockMvc.perform(post(this.recipesEndPoint + "/" + recipeUuid + this.instructionsEndPoint).contentType(
                                MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .with(asUser()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.instruction").exists());

        mockMvc.perform(get(this.recipesEndPoint + "/" + recipeUuid).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions.length()").value(1));
    }

    @Test
    void testAddRecipeInstructionWithNull() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddRecipeInstructionCommand command = new AddRecipeInstructionCommand(
                null);

        mockMvc.perform(post(this.recipesEndPoint + "/" + recipeUuid + this.instructionsEndPoint).contentType(
                                MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .with(asUser()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testAddRecipeInstructionWithBlank() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddRecipeInstructionCommand command = new AddRecipeInstructionCommand("");

        mockMvc.perform(post(this.recipesEndPoint + "/" + recipeUuid + this.instructionsEndPoint).contentType(
                                MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .with(asUser()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeInstructionShouldUpdateInstruction() throws Exception {

        UpdateRecipeInstructionCommand command = new UpdateRecipeInstructionCommand(
                RecipeFixture.newInstruction().getInstruction());

        mockMvc.perform(patch(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(2)
                        .getUuid()
                        .uuid()).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instruction").exists());

        mockMvc.perform(get(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions[2].instruction").value(
                        command.instruction()));
    }

    @Test
    void testUpdateRecipeInstructionWithNull() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddRecipeInstructionCommand command = new AddRecipeInstructionCommand(
                null);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + recipeUuid + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(2)
                        .getUuid()
                        .uuid()).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testUpdateRecipeInstructionWithBlank() throws Exception {
        UUID recipeUuid = emptyRecipe.getUuid().uuid();

        AddRecipeInstructionCommand command = new AddRecipeInstructionCommand("");

        mockMvc.perform(patch(this.recipesEndPoint + "/" + recipeUuid + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(2)
                        .getUuid()
                        .uuid()).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());
    }

    @Test
    void testMoveRecipeInstructionShouldMoveInstructionUp() throws Exception {
        int fromPos = 0;
        int toPos = 2;
        String instruction = this.recipeWithInstructions.getInstructions()
                .get(fromPos)
                .getInstruction();

        MoveRecipeInstructionCommand command = new MoveRecipeInstructionCommand(
                toPos);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(fromPos)
                        .getUuid()
                        .uuid() + this.positionEndpoint).with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").exists());

        mockMvc.perform(get(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions[" + toPos + "].instruction").value(
                        instruction))
                .andReturn();

    }

    @Test
    void testMoveRecipeInstructionShouldMoveInstructionDown() throws Exception {
        int fromPos = 2;
        int toPos = 0;
        String instruction = this.recipeWithInstructions.getInstructions()
                .get(fromPos)
                .getInstruction();

        MoveRecipeInstructionCommand command = new MoveRecipeInstructionCommand(
                toPos);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(fromPos)
                        .getUuid()
                        .uuid() + this.positionEndpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").exists());

        mockMvc.perform(get(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions[" + toPos + "].instruction").value(
                        instruction));
    }

    @Test
    void testMoveRecipeInstructionShouldFailWithOutOfBoundsBelow() throws Exception {
        int fromPos = 0;
        int toPos = -1;

        MoveRecipeInstructionCommand command = new MoveRecipeInstructionCommand(
                toPos);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(fromPos)
                        .getUuid()
                        .uuid() + this.positionEndpoint).contentType(MediaType.APPLICATION_JSON)
                        .with(asUser())
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());

    }

    @Test
    void testMoveRecipeInstructionShouldFailInstructionNotFound() throws Exception {
        int toPos = 1;

        MoveRecipeInstructionCommand command = new MoveRecipeInstructionCommand(
                toPos);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + RecipeFixture.badUuid()
                        .uuid() + this.positionEndpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .with(asUser()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(this.message).exists());

    }

    @Test
    void testMoveRecipeInstructionShouldFailWithOutOfBoundsAbove() throws Exception {
        int fromPos = 0;
        int toPos = 5;

        MoveRecipeInstructionCommand command = new MoveRecipeInstructionCommand(
                toPos);

        mockMvc.perform(patch(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(fromPos)
                        .getUuid()
                        .uuid() + this.positionEndpoint).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .with(asUser()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(this.message).exists());

    }

    @Test
    void testDeleteRecipeInstructionShouldDeleteInstruction() throws Exception {
        recipeRepository.save(this.recipeWithInstructions);

        mockMvc.perform(delete(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + this.recipeWithInstructions.getInstructions()
                        .get(1)
                        .getUuid()
                        .uuid()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").exists());

        mockMvc.perform(get(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions.length()").value(2));
    }

    @Test
    void testDeleteRecipeInstructionShouldFailInstructionNotFound() throws Exception {
        mockMvc.perform(delete(this.recipesEndPoint + "/" + this.recipeWithInstructions.getUuid()
                        .uuid()
                        .toString() + this.instructionsEndPoint + "/" + RecipeFixture.badUuid()
                        .uuid()).with(asUser()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(this.message).exists());

    }
}