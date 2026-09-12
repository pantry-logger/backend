package com.pantrylogger.domain.user;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> getByEmail(Email email);

    Optional<User> getByUsername(Username username);

    Optional<User> getByExternalAuthId(ExternalAuthId externalAuthId);

    User save(User user);

    void delete(User uuid);

}
