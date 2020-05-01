package com.jashburn.javafeatures.java8.lambdas.advancedcollections;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class ElementOrdering {

    @Test
    void orderWithListAndSet() {
        List<Integer> originalList = IntStream.range(0, 10).boxed().collect(toList());
        Collections.reverse(originalList);

        List<Integer> incrementedList = IntStream.range(2, 12).boxed().collect(toList());
        Collections.reverse(incrementedList);

        assertAll("Collection with and without defined order", () -> {
            List<Integer> stillOrdered = originalList.stream().map(i -> i + 2).collect(toList());
            assertEquals(incrementedList, stillOrdered);
        }, () -> {
            // OpenJDK (build 14+36-1461) seems to create the set in ascending order for integers
            Set<Integer> originalSet = new HashSet<>(originalList);
            List<Integer> notOrdered = originalSet.stream().map(i -> i + 2).collect(toList());
            assertNotEquals(incrementedList, notOrdered);
        });
    }
}
