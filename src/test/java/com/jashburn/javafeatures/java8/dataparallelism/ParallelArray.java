package com.jashburn.javafeatures.java8.dataparallelism;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.util.Arrays;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class ParallelArray {

    @Test
    void parallelPrefix() {
        double[] values = {0, 1, 2, 3, 4, 3.5};
        final int windowLength = 3;

        double[] cumulativeSums = Arrays.copyOf(values, values.length);
        Arrays.parallelPrefix(cumulativeSums, Double::sum);
        assertArrayEquals(new double[] {0, 1, 3, 6, 10, 13.5}, cumulativeSums);

        final int startIndex = windowLength - 1;
        final double[] movingAverages = IntStream.range(startIndex, cumulativeSums.length)
                .mapToDouble(integer -> {
                    final double sumBeforeWindow = isFirstValue(startIndex, integer) ? 0
                            : cumulativeSums[integer - windowLength];
                    final double sumWithinWindow = cumulativeSums[integer] - sumBeforeWindow;
                    return sumWithinWindow / windowLength;
                }).toArray();

        assertArrayEquals(new double[] {1, 2, 3, 3.5}, movingAverages);
    }

    private boolean isFirstValue(final int startInt, final int integer) {
        return integer == startInt;
    }
}
