package com.jashburn.javafeatures.java8.lambdas.streams;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.jashburn.javafeatures.java8.lambdas.Track;
import org.junit.jupiter.api.Test;

class CommonStreamOperations {

    @Test
    void collectToList() {
        List<String> collected = Stream.of("a", "b", "c").collect(Collectors.toList());
        assertIterableEquals(Arrays.asList("a", "b", "c"), collected);
    }

    @Test
    void mapStringToInteger() {
        List<Integer> collected = Stream.of("1", "2", "3")
                .map(str -> Integer.valueOf(str))
                .collect(Collectors.toList());
        assertIterableEquals(Arrays.asList(1, 2, 3), collected);
    }

    @Test
    void filterStringStartsWithDigit() {
        List<String> startWithDigit = Stream.of("abc", "1bc", "a2c", "ab3", "12c", "123")
                .filter(str -> Character.isDigit(str.charAt(0)))
                .collect(Collectors.toList());
        assertIterableEquals(Arrays.asList("1bc", "12c", "123"), startWithDigit);
    }

    @Test
    void flatMapConcatListsToList() {
        List<Integer> concatenated = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4))
                .flatMap(nums -> nums.stream())
                .collect(Collectors.toList());
        assertIterableEquals(Arrays.asList(1, 2, 3, 4), concatenated);
    }

    @Test
    void findShortestAndLongestTrack() {
        List<Track> tracks = Arrays.asList(new Track("Bakai", 524),
                new Track("Violets for Your Furs", 378), new Track("Time Was", 451));

        assertAll(() -> {
            Track shortestTrack =
                    tracks.stream().min(Comparator.comparing(track -> track.getLength())).get();
            assertEquals(tracks.get(1), shortestTrack);
        }, () -> {
            // comparingInt requires a ToIntFunction arg - useful when an integer sort key is needed
            Track longestTrack =
                    tracks.stream().max(Comparator.comparingInt(track -> track.getLength())).get();
            assertEquals(tracks.get(0), longestTrack);
        });
    }

    @Test
    void reduceAccumulator() {
        int productOfInts = IntStream.range(1, 6).reduce((acc, i) -> acc * i).getAsInt();
        assertEquals(120, productOfInts);
    }

    /**
     * The 3-parameter <code>reduce()</code> method is a combination of map and reduce operations.
     * In this case, it is necessary to convert string to integer before the multiplication, hence
     * the {@link BiFunction}. The combiner {@link BinaryOperator} (3rd parameter) is specifically
     * needed in parallel streams to multiply (combine) the various split (intermediate) results
     * together at the end.
     * <p>
     * The overall operation needs to work for both sequential and parallel streams without changes,
     * which necessitates the combiner.
     * 
     * @see <a href="https://www.logicbig.com/tutorials/core-java-tutorial/java-util-stream/reduction.html">Java 8 Stream - Reduction</a>
     */
    @Test
    void reduceParallelAccumulatorCombiner() {
        int reduceResult = Stream.of("2", "3", "4", "5").parallel().reduce(1,
                (acc, i) -> acc * Integer.parseInt(i),
                (acc1, acc2) -> acc1 * acc2);
        assertEquals(120, reduceResult);

        /*
         * Many reductions using this form can be represented more simply by an explicit combination
         * of map and reduce operations. The accumulator function (above) acts as a fused mapper and
         * accumulator, which can sometimes be more efficient than separate mapping and reduction,
         * such as when knowing the previously reduced value allows you to avoid some computation.
         */

        int mapReduceResult = Stream.of("2", "3", "4", "5").parallel()
                .map(str -> Integer.parseInt(str))
                .reduce(1, (acc, i) -> acc * i);
        assertEquals(120, mapReduceResult);
    }
}
