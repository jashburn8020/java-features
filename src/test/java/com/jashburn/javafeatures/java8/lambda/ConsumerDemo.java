package com.jashburn.javafeatures.java8.lambda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * {@link Consumer} accepts an argument of type <code>T</code> and does not return anything.
 * <p>
 * <code>T => Consumer</code>
 */
class ConsumerDemo {

    private Consumer<String> reporter;
    private String reportedValue;

    @BeforeEach
    void setUp() {
        reporter = str -> reportedValue = str;
    }

    @Test
    @DisplayName("Consumer test")
    void directConsumer(TestInfo info) {
        reporter.accept(info.getDisplayName());
        assertEquals("Consumer test", reportedValue);
    }

    @Test
    @DisplayName("Composed consumer test")
    void composedConsumer(TestInfo info) {
        reporter.andThen(str -> reportedValue += " with andThen").accept(info.getDisplayName());
        assertEquals("Composed consumer test with andThen", reportedValue);
    }
}
