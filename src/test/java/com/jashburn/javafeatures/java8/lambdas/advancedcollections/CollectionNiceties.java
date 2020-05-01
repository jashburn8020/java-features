package com.jashburn.javafeatures.java8.lambdas.advancedcollections;

import static com.jashburn.javafeatures.java8.lambdas.SampleData.johnLennon;
import static com.jashburn.javafeatures.java8.lambdas.SampleData.paulMcCartney;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jashburn.javafeatures.java8.lambdas.Artist;
import org.junit.jupiter.api.Test;

class CollectionNiceties {

    private Map<String, Artist> db;

    @Test
    void mapComputeIfAbsent() {
        db = Map.of(paulMcCartney.getName(), paulMcCartney);

        Map<String, Artist> artistCache = new HashMap<>();
        artistCache.put(johnLennon.getName(), johnLennon);

        assertAll(() -> {
            assertEquals(johnLennon,
                    artistCache.computeIfAbsent("John Lennon", this::getArtistFromDB));
        }, () -> {
            assertEquals(paulMcCartney,
                    artistCache.computeIfAbsent("Paul McCartney", this::getArtistFromDB));
            assertEquals(paulMcCartney, artistCache.get("Paul McCartney"));
        }, () -> {
            assertNull(artistCache.computeIfAbsent("Ringo Starr", this::getArtistFromDB));
            assertFalse(artistCache.containsKey("Ringo Starr"));
        });
    }

    private Artist getArtistFromDB(String name) {
        return db.get(name);
    }

    @Test
    void mapMerge() {
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("one", new ArrayList<>(List.of(1)));

        assertAll(() -> {
            List<Integer> newList =
                    map.merge("one", new ArrayList<>(List.of(2, 3)), this::mergeLists);
            assertIterableEquals(List.of(1, 2, 3), newList);
            assertEquals(List.of(1, 2, 3), map.get("one"));
        }, () -> {
            List<Integer> newList =
                    map.merge("two", new ArrayList<>(List.of(2, 3)), this::mergeLists);
            assertIterableEquals(List.of(2, 3), newList);
            assertEquals(List.of(2, 3), map.get("two"));
        });
    }

    private List<Integer> mergeLists(List<Integer> oldList, List<Integer> currentList) {
        if (currentList == null)
            return null;
        oldList.addAll(currentList);
        return oldList;
    }
}
