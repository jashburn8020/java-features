package com.jashburn.javafeatures.java8.testingdebuggingrefactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class TestingLambda {

    List<String> allToUpperCase(List<String> words) {
        return words.stream().map(word -> word.toUpperCase()).collect(Collectors.toList());
    }

    @Test
    void testSurroundingMethod() {
        List<String> words = Arrays.asList("a", "b", "Hello");
        assertIterableEquals(Arrays.asList("A", "B", "HELLO"), allToUpperCase(words));
    }

    // ---

    List<String> allToSentenceCase(List<String> words) {
        return words.stream()
                // Pretend this uses a complex lambda expression
                .map(word -> {
                    char firstChar = word.charAt(0);
                    if (Character.isUpperCase(firstChar)) {
                        return word;
                    }
                    return Character.toUpperCase(firstChar) + word.substring(1);
                }).collect(Collectors.toList());
    }

    List<String> allToSentenceCaseLambdaExtracted(List<String> words) {
        return words.stream().map(this::toSentenceCase).collect(Collectors.toList());
    }

    private String toSentenceCase(String word) {
        char firstChar = word.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return word;
        }
        return Character.toUpperCase(firstChar) + word.substring(1);
    }

    @TestFactory
    Stream<DynamicNode> testExtractedLambda() {
        return Stream.of("a", "ab", "abc", "A", "AB").map(word -> dynamicTest(word, () -> {
            String result = toSentenceCase(word);
            assertEquals(Character.toUpperCase(word.charAt(0)), result.charAt(0));
            assertEquals(word.substring(1), result.substring(1));
        }));
    }

    @Test
    void testSurroundingMethodSentenceCase() {
        List<String> words = Arrays.asList("a", "ab", "abc", "A", "AB");
        List<String> expectedWords = Arrays.asList("A", "Ab", "Abc", "A", "AB");
        assertIterableEquals(expectedWords, allToSentenceCase(words));
        assertIterableEquals(expectedWords, allToSentenceCaseLambdaExtracted(words));        
    }
}
