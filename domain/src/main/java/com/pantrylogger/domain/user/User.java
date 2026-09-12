package com.pantrylogger.domain.user;

import java.util.UUID;

public class User {

    private final UserUUID uuid;
    private final ExternalAuthId externalAuthId;
    private Email email;
    private Username username;
    private String firstName;
    private String lastName;

    public record UserUUID(UUID uuid) {
        public UserUUID(String strUUID) {
            this(UUID.fromString(strUUID));
        }

        public UserUUID() {
            this(UUID.randomUUID());
        }
    }

    public User(
            UserUUID uuid,
            ExternalAuthId externalAuthId,
            Email email,
            Username username,
            String firstName,
            String lastName
    ) {
        this.uuid = uuid;
        this.externalAuthId = externalAuthId;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(
            ExternalAuthId externalAuthId,
            Email email,
            Username username,
            String firstName,
            String lastName
    ) {
        this.uuid = new UserUUID();
        this.externalAuthId = externalAuthId;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserUUID getUuid() {
        return uuid;
    }

    public ExternalAuthId getExternalAuthId() {
        return externalAuthId;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
