package com.pantrylogger.postgresadapter.user;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pantrylogger.domain.user.Email;
import com.pantrylogger.domain.user.ExternalAuthId;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.domain.user.Username;

@Service
public class UserPostgresAdapter implements UserRepositoryPort {
    private final UserJpaEntityRepository userJpaEntityRepository;

    public UserPostgresAdapter(UserJpaEntityRepository userJpaEntityRepository) {
        this.userJpaEntityRepository = userJpaEntityRepository;
    }

    @Override
    public Optional<User> getByEmail(Email email) {
        return this.userJpaEntityRepository.findByEmail(email.toString())
                .map(UserJpaEntity::toUser);
    }

    @Override
    public Optional<User> getByUsername(Username username) {
        return this.userJpaEntityRepository.findByUsername(username.toString())
                .map(UserJpaEntity::toUser);
    }

    @Override
    public Optional<User> getByExternalAuthId(ExternalAuthId externalAuthId) {
        return this.userJpaEntityRepository.findByExternalAuthId(externalAuthId.value())
                .map(UserJpaEntity::toUser);
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = new UserJpaEntity(user);

        return this.userJpaEntityRepository.save(userJpaEntity).toUser();
    }

    @Override
    public void delete(User user) {
        try {
            userJpaEntityRepository.deleteById(user.getUuid().uuid());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(
                    String.format(
                            "Invariant violated: user with UUID %s was expected to exist but was not found",
                            user.getUuid().uuid()
                    ),
                    e
            );
        }
    }
}
