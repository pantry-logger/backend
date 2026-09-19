package com.pantrylogger.shoppinglist;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

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

import com.pantrylogger.domain.ShoppingListFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.shoppinglist.update.UpdateShoppingListCommand;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.restapi.security.CustomUserDetails;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ShoppingListsUpdateIntegrationTest {
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
    private ShoppingListRepositoryPort shoppingListRepository;

    @Autowired
    private UserRepositoryPort userRepository;

    private User testUser;
    private User anotherTestUser;

    private ShoppingList emptyShoppingList;

    private final String shoppingListsEndPoint = "/shopping-list";

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
        this.emptyShoppingList = ShoppingListFixture.emptyShoppingList(Set.of(
                testUser.getUsername()));

        shoppingListRepository.save(this.emptyShoppingList);
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
    void testUpdateShoppingListForCorrectUser() throws Exception {
        String updatedShoppingListName = "Household";
        UpdateShoppingListCommand updateShoppingListCommand = new UpdateShoppingListCommand(
                updatedShoppingListName);
        mockMvc.perform(patch(this.shoppingListsEndPoint + "/" + this.emptyShoppingList.getUuid()
                        .uuid()
                        .toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                updateShoppingListCommand))
                        .with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(this.emptyShoppingList.getUuid()
                        .uuid()
                        .toString()))
                .andExpect(jsonPath("$.name").value(updatedShoppingListName));
    }

    @Test
    void testUpdateShoppingListForWrongUserIsNotFound() throws Exception {
        String updatedShoppingListName = "Household";
        UpdateShoppingListCommand updateShoppingListCommand = new UpdateShoppingListCommand(
                updatedShoppingListName);
        mockMvc.perform(patch(this.shoppingListsEndPoint + "/" + this.emptyShoppingList.getUuid()
                        .uuid()
                        .toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                updateShoppingListCommand))
                        .with(asAnotherUser()))
                .andExpect(status().isNotFound());
    }
}
