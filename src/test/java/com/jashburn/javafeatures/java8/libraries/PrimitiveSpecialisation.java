package com.jashburn.javafeatures.java8.libraries;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.jashburn.javafeatures.java8.lambda.Album;
import com.jashburn.javafeatures.java8.lambda.Artist;
import com.jashburn.javafeatures.java8.lambda.Track;
import org.junit.jupiter.api.Test;

class PrimitiveSpecialisation {

    /**
     * Using the specialised {@link Stream#mapToInt(java.util.function.ToIntFunction)} allows us to
     * call {@link IntStream#summaryStatistics()}.
     * <p>
     * It's also possible to calculate the individual summary statistics through
     * {@link IntStream#min()}, {@link IntStream#max()}, {@link IntStream#average()}, and
     * {@link IntStream#sum()} methods.
     */
    @Test
    void trackLengthStatistics() {
        List<Track> tracks = Arrays.asList(
                new Track("a", 60),
                new Track("b", 60),
                new Track("c", 30),
                new Track("d", 90));
        List<Artist> artists = Arrays.asList(new Artist("aa", "UK"));
        Album album = new Album("The Album", tracks, artists);

        IntSummaryStatistics trackLengthStats = album.getTracks()
                .mapToInt(track -> track.getLength())
                .summaryStatistics();

        assertAll(() -> assertEquals(90, trackLengthStats.getMax()),
                () -> assertEquals(30, trackLengthStats.getMin()),
                () -> assertEquals(60, trackLengthStats.getAverage()),
                () -> assertEquals(240, trackLengthStats.getSum()));
    }
}