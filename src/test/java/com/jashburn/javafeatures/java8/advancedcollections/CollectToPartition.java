package com.jashburn.javafeatures.java8.advancedcollections;

import static com.jashburn.javafeatures.java8.lambda.SampleData.johnColtrane;
import static com.jashburn.javafeatures.java8.lambda.SampleData.theBeatles;
import static com.jashburn.javafeatures.java8.lambda.SampleData.threeMemberBand;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.jashburn.javafeatures.java8.lambda.Artist;
import org.junit.jupiter.api.Test;

class CollectToPartition {

    @Test
    void partitioningCollector() {
        Map<Boolean, List<Artist>> partitionedArtists =
                Stream.of(johnColtrane, theBeatles, threeMemberBand)
                        .collect(partitioningBy(Artist::isSolo));

        assertAll("true: solo, false: band", () -> {
            List<Artist> soloists = Arrays.asList(johnColtrane);
            assertIterableEquals(soloists, partitionedArtists.get(true));
        }, () -> {
            List<Artist> bands = Arrays.asList(theBeatles, threeMemberBand);
            assertIterableEquals(bands, partitionedArtists.get(false));
        });
    }

    @Test
    void groupingCollector() {
        Map<String, List<Artist>> groupedArtists =
                Stream.of(johnColtrane, theBeatles, threeMemberBand)
                        .collect(groupingBy(Artist::getNationality));

        assertAll("US, UK, AU", () -> {
            assertIterableEquals(Arrays.asList(johnColtrane), groupedArtists.get("US"));
        }, () -> {
            assertIterableEquals(Arrays.asList(theBeatles), groupedArtists.get("UK"));
        }, () -> {
            assertIterableEquals(Arrays.asList(threeMemberBand), groupedArtists.get("AU"));
        });
    }
}
