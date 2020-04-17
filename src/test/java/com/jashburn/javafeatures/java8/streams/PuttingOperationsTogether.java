package com.jashburn.javafeatures.java8.streams;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.jashburn.javafeatures.java8.lambda.Album;
import com.jashburn.javafeatures.java8.lambda.Artist;
import com.jashburn.javafeatures.java8.lambda.Track;
import org.junit.jupiter.api.Test;

class PuttingOperationsTogether {

    @Test
    void albumBandNationality() {
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

        Set<String> nationalities = album.getAllMusicians()
                .filter(artist -> artist.getName().startsWith("The "))
                .map(artist -> artist.getNationality())
                .collect(Collectors.toSet());

        assertEquals(Set.of("Canada", "Sweden"), nationalities);
    }

    @Test
    void refactor() {
        List<Track> tracks = Arrays.asList(
                new Track("a", 60),
                new Track("b", 61),
                new Track("c", 30),
                new Track("d", 90));
        List<Artist> artists = Arrays.asList(new Artist("aa", "UK"));
        Album album1 = new Album("The Album", tracks, artists);

        List<Track> tracks2 = Arrays.asList(
                new Track("a2", 160),
                new Track("b", 61),
                new Track("c2", 90),
                new Track("d2", 30));
        List<Artist> artists2 = Arrays.asList(new Artist("aa", "UK"));
        Album album2 = new Album("The Album", tracks2, artists2);

        Set<String> longTracks = Set.of("b", "d", "a2", "c2");
        assertAll(
                () -> assertEquals(longTracks,
                        findLongTracksForLoop(Arrays.asList(album1, album2))),
                () -> assertEquals(longTracks,
                        findLongTracksStream(Arrays.asList(album1, album2))));
    }

    private Set<String> findLongTracksForLoop(List<Album> albums) {
        Set<String> trackNames = new HashSet<>();
        for (Album album : albums) {
            for (Track track : album.getTrackList()) {
                if (track.getLength() > 60) {
                    String name = track.getName();
                    trackNames.add(name);
                }
            }
        }
        return trackNames;
    }

    private Set<String> findLongTracksStream(List<Album> albums) {
        return albums.stream()
                .flatMap(album -> album.getTracks())
                .filter(track -> track.getLength() > 60)
                .map(track -> track.getName())
                .collect(Collectors.toSet());
    }
}
