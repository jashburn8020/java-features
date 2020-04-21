package com.jashburn.javafeatures.java8.advancedcollections;

import static com.jashburn.javafeatures.java8.lambda.SampleData.theBeatles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Collectors;
import com.jashburn.javafeatures.java8.lambda.Artist;
import org.junit.jupiter.api.Test;

class JoiningStrings {

    @Test
    void joiningCollector() {
        String beatlesMembers = theBeatles.getMembers().map(Artist::getName)
                .collect(Collectors.joining(", ", "[", "]"));

        assertEquals("[John Lennon, Paul McCartney, George Harrison, Ringo Starr]", beatlesMembers);
    }
}
