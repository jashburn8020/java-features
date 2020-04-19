package com.jashburn.javafeatures.java8.optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class OptionalDemo {

    @Test
    void presentValue() {
        Optional<Integer> optional = Optional.of(1);

        assertTrue(optional.isPresent());
        assertEquals(1, optional.get());
    }

    @Test
    void emptyValue() {
        Optional<Integer> optional = Optional.empty();

        assertFalse(optional.isPresent());
        assertThrows(NoSuchElementException.class, () -> optional.get());
    }

    @Test
    void orElseValue() {
        assertAll(() -> {
            Optional<Integer> optionalPresent = createOptional(true);
            assertEquals(1, optionalPresent.orElse(2));
            assertEquals(1, optionalPresent.orElseGet(() -> 2));
        }, () -> {
            Optional<Integer> optionalEmpty = createOptional(false);
            assertEquals(0, optionalEmpty.orElse(0));
            assertEquals(0, optionalEmpty.orElseGet(() -> 0));
        });
    }

    private Optional<Integer> createOptional(boolean present) {
        return Optional.ofNullable(present ? 1 : null);
    }
}
