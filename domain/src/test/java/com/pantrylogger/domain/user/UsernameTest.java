package com.pantrylogger.domain.user;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.pantrylogger.domain.exception.InvalidUsernameException;

class UsernameTest {

    @Test
    void nullUsernameThrowsException() {
        assertThrows(
                InvalidUsernameException.class, () -> {
                    new Username(null);
                }
        );
    }

    @Test
    void emptyUsernameThrowsException() {
        assertThrows(
                InvalidUsernameException.class, () -> {
                    new Username("");
                }
        );
    }

    @Test
    void invalidUsernameThrowsException() {
        assertThrows(
                InvalidUsernameException.class, () -> {
                    new Username("test test");
                }
        );
    }
}
