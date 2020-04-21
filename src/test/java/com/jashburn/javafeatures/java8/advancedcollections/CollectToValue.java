package com.jashburn.javafeatures.java8.advancedcollections;

import static com.jashburn.javafeatures.java8.lambda.SampleData.fourTrackAlbum;
import static com.jashburn.javafeatures.java8.lambda.SampleData.manyTrackAlbum;
import static com.jashburn.javafeatures.java8.lambda.SampleData.sampleShortAlbum;
import static com.jashburn.javafeatures.java8.lambda.SampleData.theBeatles;
import static com.jashburn.javafeatures.java8.lambda.SampleData.threeMemberBand;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.averagingLong;
import static java.util.stream.Collectors.maxBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import java.util.stream.Stream;
import com.jashburn.javafeatures.java8.lambda.Artist;
import org.junit.jupiter.api.Test;

class CollectToValue {

    /**
     * Uses:
     * <ul>
     * <li>Stream.collect(Collector) : Optional</li>
     * <li>Collectors.maxBy(Comparator) : Collector</li>
     * <li>Comparator.comparingLong(ToLongFunction) : Comparator</li>
     * </ul>
     */
    @Test
    void maxByCollector() {
        Optional<Artist> mostMembers = Stream.of(theBeatles, threeMemberBand)
                .collect(maxBy(comparingLong(artist -> artist.getMembers().count())));

        assertTrue(mostMembers.isPresent());
        assertEquals("The Beatles", mostMembers.get().getName());
    }

    /**
     * Uses:
     * <ul>
     * <li>Stream.collect(Collector) : Double</li>
     * <li>Collectors.averagingLong(ToLongFunction) : Collector</li>
     * </ul>
     */
    @Test
    void averagingIntCollector() {
        double avgNumTracks = Stream.of(sampleShortAlbum, manyTrackAlbum, fourTrackAlbum)
                .collect(averagingLong(album -> album.getTracks().count()));

        assertEquals(3.33, avgNumTracks, 0.01);
    }
}
