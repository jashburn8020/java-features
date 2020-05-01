package com.jashburn.javafeatures.java8.lambdas.testingdebuggingrefactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.jashburn.javafeatures.java8.lambdas.Album;
import com.jashburn.javafeatures.java8.lambdas.Artist;
import com.jashburn.javafeatures.java8.lambdas.Track;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

class LoggingAndPrinting {

    @Test
    void peekToLogIntermediateValues(TestReporter reporter) {
        List<Track> tracks = Arrays.asList(
                new Track("a", 1),
                new Track("b", 2),
                new Track("c", 3),
                new Track("d", 4));
        List<Artist> artists = Arrays.asList(
                new Artist("aa", "UK"),
                new Artist("The bb", "Sweden"),
                new Artist("The cc", "Canada"),
                new Artist("The dd", "Sweden"));
        Album album = new Album("The Album", tracks, artists);

        Set<String> nationalities = album.getAllMusicians().parallel()
                .filter(artist -> artist.getName().startsWith("The "))
                .peek(artist -> reporter.publishEntry("name", artist.getName()))
                .map(artist -> artist.getNationality())
                .peek(nationality -> reporter.publishEntry("nationality", nationality))
                .collect(Collectors.toSet());

        assertEquals(Set.of("Canada", "Sweden"), nationalities);
    }
}