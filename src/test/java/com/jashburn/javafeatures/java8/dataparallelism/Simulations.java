package com.jashburn.javafeatures.java8.dataparallelism;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class Simulations {

    private static final int REPETITIONS = 1_000_000;
    private static final double FRACTION = 1.0 / REPETITIONS;

    /**
     * Simulate 2-dice rolls, collecting result into a map of total dice value (key) and probability
     * of the total (value).
     * <p>
     * Probability = number of times for a particular total / REPETITIONS
     * <p>
     * Since the summingDouble collector sums the return value of its mapper function, the function
     * only needs to return the fraction of repetitions (1 / REPETITIONS), which when summed up,
     * gives us the probability above.
     */
    @Test
    void parallelDiceRollsSimulation() {
        Map<Integer, Double> probabilities =
                IntStream.range(0, REPETITIONS).parallel().mapToObj(i -> {
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    return random.nextInt(1, 7) + random.nextInt(1, 7);
                }).collect(Collectors.groupingBy(diceTotal -> diceTotal,
                        Collectors.summingDouble(diceTotal -> FRACTION)));

        probabilities.forEach((key, value) -> {
            assertEquals(expectedProbability(key), value, 0.001,
                    () -> String.format("Abnormal probability for %d", key));
        });
    }

    @Test
    void expectedProbabilityTest() {
        double single = 1.0 / 36.0;

        assertAll(() -> assertEquals(single, expectedProbability(2), 0.001, "2"),
                () -> assertEquals(3 * single, expectedProbability(4), 0.001, "4"),
                () -> assertEquals(6 * single, expectedProbability(7), 0.001, "7"),
                () -> assertEquals(5 * single, expectedProbability(8), 0.001, "8"),
                () -> assertEquals(3 * single, expectedProbability(10), 0.001, "10"),
                () -> assertEquals(single, expectedProbability(12), 0.001, "12"));
    }

    /**
     * Distribution of permutations for the total value with 2 dice:
     * 
     * <pre>
    2	11
    3	12, 21
    4	13, 31, 22
    5	14, 41, 23, 32
    6	15, 51, 24, 42, 33
    7	16, 61, 25, 52, 34, 43
    8	26, 62, 35, 53, 44
    9	36, 63, 45, 54
    10	46, 64, 55
    11	56, 65
    12	66
     * </pre>
     */
    private double expectedProbability(int diceTotal) {
        double singleProbability = 1.0 / 36.0; // 36.0 is the number of permutations for 2 dice
        int midpointTotal = 2 + (12 - 1) / 2;

        if (diceTotal <= midpointTotal)
            return (diceTotal - 1) * singleProbability;

        return (6 - (diceTotal - midpointTotal)) * singleProbability;
    }
}
