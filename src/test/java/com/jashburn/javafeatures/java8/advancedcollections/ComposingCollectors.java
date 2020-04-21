package com.jashburn.javafeatures.java8.advancedcollections;

import static com.jashburn.javafeatures.java8.lambda.SampleData.aLoveSupreme;
import static com.jashburn.javafeatures.java8.lambda.SampleData.fourTrackAlbum;
import static com.jashburn.javafeatures.java8.lambda.SampleData.johnColtrane;
import static com.jashburn.javafeatures.java8.lambda.SampleData.manyTrackAlbum;
import static com.jashburn.javafeatures.java8.lambda.SampleData.sampleShortAlbum;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.jashburn.javafeatures.java8.lambda.Album;
import com.jashburn.javafeatures.java8.lambda.Artist;
import org.junit.jupiter.api.Test;

class ComposingCollectors {

    /**
     * Count the number of albums for each artist.
     * <p>
     * Uses:
     * <ul>
     * <li>Stream.collect(Collector) : Map</li>
     * <li>Collectors.groupingBy(Function, Collector) : Collector</li>
     * <li>Collectors.counting() : Collector</li>
     * </ul>
     * <code>groupingBy</code> divides elements into buckets. Each bucket gets associated with the
     * key provided by the classifier function: <code>getMainMusician</code>. The
     * <code>groupingBy</code> operation then uses the downstream collector to collect each bucket
     * and makes a map of the results.
     */
    @Test
    void countingDownstreamCollector() {
        Map<Artist, Long> numOfAlbumsByArtist =
                getAlbums().collect(groupingBy(Album::getMainMusician, counting()));

        Map<Artist, Long> expected = Map.of(johnColtrane, 3L, new Artist("aa", "UK"), 1L);
        assertEquals(expected, numOfAlbumsByArtist);
    }

    /**
     * List the albums by name for each artist.
     * <p>
     * Uses:
     * <ul>
     * <li>Stream.collect(Collector) : Map</li>
     * <li>Collectors.groupingBy(Function, Collector) : Collector</li>
     * <li>Collectors.mapping(Function, Collector) : Collector</li>
     * <li>Collectors.toList() : Collector</li>
     * </ul>
     * The <code>mapping</code> collector is <code>groupingBy</code>'s downstream collector. It maps
     * album to the album name before passing the album name to its (further) downstream collector,
     * <code>toList</code>, to be stored in a list.
     */
    @Test
    void mappingDownstreamCollector() {
        Map<Artist, List<String>> albumNamesByArtist = getAlbums()
                .collect(groupingBy(Album::getMainMusician, mapping(Album::getName, toList())));

        List<String> johnColtraneAlbums =
                Arrays.asList("A Love Supreme", "sample Short Album", "sample Many Track Album");
        List<String> aaAlbums = Arrays.asList("The Album");
        Map<Artist, List<String>> expected =
                Map.of(johnColtrane, johnColtraneAlbums, new Artist("aa", "UK"), aaAlbums);
        assertEquals(expected, albumNamesByArtist);
    }

    private Stream<Album> getAlbums() {
        return Arrays.asList(aLoveSupreme, sampleShortAlbum, manyTrackAlbum, fourTrackAlbum)
                .stream();
    }
}
