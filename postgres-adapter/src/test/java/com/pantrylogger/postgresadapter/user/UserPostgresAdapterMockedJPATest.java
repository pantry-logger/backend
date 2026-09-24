package com.pantrylogger.postgresadapter.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;

import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.user.Email;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.Username;

class UserPostgresAdapterMockedJPATest {
    private UserPostgresAdapter adapter;
    private final UserJpaEntityRepository mockRepository = mock(
            UserJpaEntityRepository.class);

    private final User testUser = UserFixture.basicTestUser();
    private final UserJpaEntity testEntity = new UserJpaEntity(testUser);

    private final User missingUser = UserFixture.missingTestUser();
    private final Email missingEmail = UserFixture.missingEmail();
    private final Username missingUsername = UserFixture.missingUsername();

    @BeforeEach
    void setup() {
        this.adapter = new UserPostgresAdapter(this.mockRepository);

        when(this.mockRepository.findByEmail(this.testUser.getEmail()
                .toString()))
                .thenReturn(Optional.of(this.testEntity));
        when(this.mockRepository.findByEmail(this.missingEmail
                .toString()))
                .thenReturn(Optional.empty());
        when(this.mockRepository.findByUsername(this.testUser.getUsername()
                .toString()))
                .thenReturn(Optional.of(this.testEntity));
        when(this.mockRepository.findByUsername(this.missingUsername
                .toString()))
                .thenReturn(Optional.empty());
        when(this.mockRepository.save(
                Mockito.any(UserJpaEntity.class)))
                .thenReturn(this.testEntity);
    }

    @Test
    void getByEmailShouldReturnMappedRecipe() {
        Optional<User> optionalUser = this.adapter.getByEmail(testUser.getEmail());
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals(this.testUser.getEmail(), user.getEmail());
        assertEquals(this.testUser.getUsername(), user.getUsername());
    }

    @Test
    void getByWrongEmailShouldReturnEmptyOptional() {
        Optional<User> optionalUser = this.adapter.getByEmail(missingUser.getEmail());
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void getByUsernameShouldReturnMappedRecipe() {
        Optional<User> optionalUser = this.adapter.getByUsername(testUser.getUsername());
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals(this.testUser.getEmail(), user.getEmail());
        assertEquals(this.testUser.getUsername(), user.getUsername());
    }

    @Test
    void getByWrongUsernameShouldReturnEmptyOptional() {
        Optional<User> optionalUser = this.adapter.getByUsername(missingUser.getUsername());
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void saveShouldReturnSavedUser() {
        User saved = this.adapter.save(this.testUser);
        assertEquals(this.testUser.getEmail(), saved.getEmail());
        assertEquals(this.testUser.getUsername(), saved.getUsername());
    }

    @Test
    void deleteWithBadIDShouldThrowException() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(this.mockRepository)
                .deleteById(missingUser.getUuid().uuid());

        assertThrows(
                IllegalStateException.class,
                () -> this.adapter.delete(this.missingUser)
        );
    }
}