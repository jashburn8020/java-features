package com.jashburn.javafeatures.java8.lambdas.designarchitecture.singleresponsibilityprinciple;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class CountPrimes {

    /**
     * This has 2 responsibilities: counting numbers with a certain property, and checking whether a
     * number is a prime.
     */
    long countPrimesMultiResp(int upTo) {
        long tally = 0;
        for (int number = 2; number < upTo; number++) {
            boolean isPrime = true;
            for (int divisor = 2; divisor < (number / 2) + 1; divisor++) {
                if (number % divisor == 0) {
                    isPrime = false;
                }
            }
            if (isPrime) {
                tally++;
            }
        }
        return tally;
    }

    /**
     * Checking whether a number is a prime has been refactored out.
     * <p>
     * This is dealing with looping over numbers. If we follow the single responsibility principle,
     * then iteration should be encapsulated elsewhere. If we want to count the number of primes for
     * a very large {@code upTo} value, then we want to be able to perform this operation in
     * parallel - the threading model is a responsibility of the code.
     */
    long countPrimesSingleResp(int upTo) {
        long tally = 0;
        for (int number = 2; number < upTo; number++) {
            if (isPrime(number)) {
                tally++;
            }
        }
        return tally;
    }

    private boolean isPrime(int number) {
        for (int divisor = 2; divisor < (number / 2) + 1; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Use the Java 8 streams library, and delegate the responsibility for controlling the loop to
     * the library itself. Use the {@code range} method to count the numbers between 0 and
     * {@code upTo}, filter them to check that they really are prime, and then count the result.
     */
    long countPrimeSingleRespLambda(int upTo) {
        return IntStream.range(2, upTo).parallel().filter(this::isPrimeLambda).count();
    }

    private boolean isPrimeLambda(int number) {
        return !IntStream.range(2, number / 2 + 1).anyMatch(divisor -> number % divisor == 0);
    }

    // ---

    @TestFactory
    Stream<DynamicNode> testCountPrimes() {
        return Stream
                .<Function<Integer, Long>>of(this::countPrimesMultiResp,
                        this::countPrimesSingleResp, this::countPrimeSingleRespLambda)
                .map(countPrimeFunc -> DynamicTest.dynamicTest("Number of primes",
                        () -> assertAll(() -> assertEquals(4, countPrimeFunc.apply(10)),
                                () -> assertEquals(8, countPrimeFunc.apply(20)),
                                () -> assertEquals(10, countPrimeFunc.apply(30)),
                                () -> assertEquals(25, countPrimeFunc.apply(100)),
                                () -> assertEquals(168, countPrimeFunc.apply(1000)))));
    }
}
