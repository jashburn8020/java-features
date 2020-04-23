package com.jashburn.javafeatures.java8.testingdebuggingrefactoring;

import static com.jashburn.javafeatures.java8.lambda.SampleData.aLoveSupreme;
import static com.jashburn.javafeatures.java8.lambda.SampleData.fourTrackAlbum;
import static com.jashburn.javafeatures.java8.lambda.SampleData.sampleShortAlbum;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;
import com.jashburn.javafeatures.java8.lambda.Album;
import com.jashburn.javafeatures.java8.lambda.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class Order {

    private List<Album> albums;

    @BeforeEach
    void setUp() {
        albums = Arrays.asList(aLoveSupreme, sampleShortAlbum, fourTrackAlbum);
    }

    long countRunningTime() {
        long count = 0;
        for (Album album : albums) {
            for (Track track : album.getTrackList()) {
                count += track.getLength();
            }
        }
        return count;
    }

    long countRunningTimeRefactored() {
        return countFeature(album -> album.getTracks().mapToLong(track -> track.getLength()).sum());
    }

    long countMusicians() {
        long count = 0;
        for (Album album : albums) {
            count += album.getMusicianList().size();
        }
        return count;
    }

    long countMusiciansRefactored() {
        return countFeature(album -> album.getAllMusicians().count());
    }

    long countTracks() {
        long count = 0;
        for (Album album : albums) {
            count += album.getTrackList().size();
        }
        return count;
    }

    long countTracksRefactored() {
        return countFeature(album -> album.getTracks().count());
    }

    private long countFeature(ToLongFunction<Album> featureFunction) {
        return albums.stream().mapToLong(featureFunction).sum();
    }

    // --- Tests ---

    @Test
    void testPassingInALambda() {
        assertEquals(15, countFeature(album -> 5));
    }

    @TestFactory
    Stream<DynamicNode> testCountRunningTime() {
        return generateDynamicNodes(Map.of("original", this::countRunningTime,
                "refactored", this::countRunningTimeRefactored), 1179);
    }

    @TestFactory
    Stream<DynamicNode> testCountMusicians() {
        return generateDynamicNodes(Map.of("original", this::countMusicians,
                "refactored", this::countMusiciansRefactored), 3);
    }

    @TestFactory
    Stream<DynamicNode> testCountTracks() {
        return generateDynamicNodes(Map.of("original", this::countTracks,
                "refactored", this::countTracksRefactored), 7);
    }

    private Stream<DynamicNode> generateDynamicNodes(Map<String, Supplier<Long>> methods,
            long expectedResult) {
        return methods.entrySet().stream()
                .map(method -> dynamicTest(method.getKey(),
                        () -> assertEquals(expectedResult, method.getValue().get())));
    }

}
