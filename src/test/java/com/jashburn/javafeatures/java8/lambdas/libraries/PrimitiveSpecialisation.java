package com.jashburn.javafeatures.java8.lambdas.libraries;

import static com.jashburn.javafeatures.java8.lambdas.SampleData.fourTrackAlbum;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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
        IntSummaryStatistics trackLengthStats =
                fourTrackAlbum.getTracks().mapToInt(track -> track.getLength()).summaryStatistics();

        assertAll(() -> assertEquals(90, trackLengthStats.getMax()),
                () -> assertEquals(30, trackLengthStats.getMin()),
                () -> assertEquals(60, trackLengthStats.getAverage()),
                () -> assertEquals(240, trackLengthStats.getSum()));
    }
}
