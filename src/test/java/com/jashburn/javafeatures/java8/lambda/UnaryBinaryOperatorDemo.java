package com.jashburn.javafeatures.java8.lambda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

/**
 * {@link UnaryOperator} is a specialisation of {@link Function}. It accepts an argument of type
 * <code>T</code> and returns an object of type <code>T</code>.
 * <p>
 * <code>T => UnaryOperator => T</code>
 * <p>
 * {@link BinaryOperator} is a specialisation of {@link BiFunction}. It accepts two arguments of
 * type <code>T</code> and returns an object of type <code>T</code>.
 * <p>
 * <code>T, T => BinaryOperator => T</code>
 */
class UnaryBinaryOperatorDemo {

    @Test
    void composeUnaryBinaryOperator() {
        BinaryOperator<Integer> multiplier = (x, y) -> x * y;
        UnaryOperator<Integer> doubler = x -> x * 2;
        BiFunction<Integer, Integer, Integer> multiplyAndDouble = multiplier.andThen(doubler);

        assertEquals(12, multiplyAndDouble.apply(2, 3));
    }
}
