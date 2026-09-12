package com.pantrylogger.postgresadapter.user;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.pantrylogger.domain.user.Email;
import com.pantrylogger.domain.user.ExternalAuthId;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.User.UserUUID;
import com.pantrylogger.domain.user.Username;

@Entity
public class UserJpaEntity {

    @Id
    private UUID uuid;
    private String externalAuthId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;

    public UserJpaEntity(User user) {
        this.uuid = user.getUuid().uuid();
        this.externalAuthId = user.getExternalAuthId().value();
        this.email = user.getEmail().address();
        this.username = user.getUsername().username();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public UserJpaEntity() {
    }

    public User toUser() {
        return new User(
                new UserUUID(this.uuid),
                new ExternalAuthId(this.externalAuthId),
                new Email(this.email),
                new Username(this.username),
                this.firstName,
                this.lastName
        );
    }
}
