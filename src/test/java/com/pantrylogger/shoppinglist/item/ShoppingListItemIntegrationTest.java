package com.pantrylogger.shoppinglist.item;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import com.pantrylogger.domain.ShoppingListFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.ingredient.IngredientRepositoryPort;
import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.restapi.security.CustomUserDetails;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ShoppingListItemIntegrationTest {
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

    @Autowired
    private IngredientRepositoryPort ingredientRepository;

    private User testUser;
    private User anotherTestUser;

    private ShoppingList emptyShoppingList;

    private ShoppingListItem shoppingListItem;

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
        ShoppingListItem tempShoppingListItem = ShoppingListFixture.aShoppingListItem(
                this.testUser.getUsername());

        this.ingredientRepository.save(tempShoppingListItem.getIngredientAmount()
                .getIngredient());

        this.emptyShoppingList.addItem(
                testUser.getUsername(),
                tempShoppingListItem.getIngredientAmount()
        );

        shoppingListRepository.save(this.emptyShoppingList);

        this.shoppingListItem = this.emptyShoppingList.getItems().getFirst();
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
    void TestMarkItemAsGotAsCorrectUser() throws Exception {
        mockMvc.perform(patch(this.shoppingListsEndPoint + "/" + this.emptyShoppingList.getUuid() + "/items/" + this.shoppingListItem.getUuid()).with(
                asUser())).andExpect(
                status().isOk());
    }

    @Test
    void TestMarkItemAsGotAsWrongUserIsNotFound() throws Exception {
        mockMvc.perform(patch(this.shoppingListsEndPoint + "/" + this.emptyShoppingList.getUuid() + "/items/" + this.shoppingListItem.getUuid()).with(
                asAnotherUser())).andExpect(
                status().isNotFound());
    }
}
