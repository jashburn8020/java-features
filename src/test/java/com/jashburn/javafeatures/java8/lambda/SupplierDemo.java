package com.jashburn.javafeatures.java8.lambda;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * {@link Supplier} does not accept arguments and returns an object of type <code>T</code>.
 * <p>
 * <code>Supplier => T</code>
 */
class SupplierDemo {

    @Test
    void directSupplier() {
        Supplier<Integer> supplier = () -> new Random().nextInt(5);
        IntStream.range(0, 10).forEach(i -> {
            int n = supplier.get();
            assertTrue(n > -1 && n < 5);
        });
    }
}
