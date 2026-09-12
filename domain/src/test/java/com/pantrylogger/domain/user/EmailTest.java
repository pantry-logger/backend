package com.pantrylogger.domain.user;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.pantrylogger.domain.exception.InvalidEmailException;

public class EmailTest {

    @Test
    void nullEmailThrowsException() {
        assertThrows(
                InvalidEmailException.class, () -> {
                    new Email(null);
                }
        );
    }

    @Test
    void emptyEmailThrowsException() {
        assertThrows(
                InvalidEmailException.class, () -> {
                    new Email("");
                }
        );
    }

    @Test
    void invalidEmailThrowsException() {
        assertThrows(
                InvalidEmailException.class, () -> {
                    new Email("test");
                }
        );
    }
}
