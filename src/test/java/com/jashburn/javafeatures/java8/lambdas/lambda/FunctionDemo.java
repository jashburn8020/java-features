package com.jashburn.javafeatures.java8.lambdas.lambda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

/**
 * {@link Function} accepts an argument of type <code>T</code> and returns an object of type
 * <code>R</code>.
 * <p>
 * <code>T => Function => R</code>
 */
class FunctionDemo {

    private Function<String, Integer> stringLength = str -> str.length();

    @Test
    void singleFunction() {
        List<String> strings = Arrays.asList("one", "two", "three");

        Map<String, Integer> result = listToMap(strings, stringLength);
        Map<String, Integer> expected = Map.of("one", 3, "two", 3, "three", 5);

        assertEquals(expected, result);
    }

    private <T, R> Map<T, R> listToMap(List<T> list, Function<T, R> mapper) {
        Map<T, R> resultMap = new HashMap<>();
        list.forEach(item -> resultMap.put(item, mapper.apply(item)));

        return resultMap;
    }

    @Test
    void andThenFunction() {
        String original = "one";
        String result = stringLength
                .andThen(length -> String.format("'%s' has %d characters", original, length))
                .apply(original);

        assertEquals("'one' has 3 characters", result);
    }
}
