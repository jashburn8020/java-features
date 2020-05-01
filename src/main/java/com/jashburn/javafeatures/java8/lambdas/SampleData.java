package com.jashburn.javafeatures.java8.lambdas;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class SampleData {

    public static final Artist johnColtrane = new Artist("John Coltrane", "US");

    public static final Artist johnLennon = new Artist("John Lennon", "UK");
    public static final Artist paulMcCartney = new Artist("Paul McCartney", "UK");
    public static final Artist georgeHarrison = new Artist("George Harrison", "UK");
    public static final Artist ringoStarr = new Artist("Ringo Starr", "UK");

    public static final List<Artist> membersOfTheBeatles =
            asList(johnLennon, paulMcCartney, georgeHarrison, ringoStarr);

    public static final Artist theBeatles = new Artist("The Beatles", membersOfTheBeatles, "UK");

    public static final Album aLoveSupreme = new Album("A Love Supreme",
            asList(new Track("Acknowledgement", 467), new Track("Resolution", 442)),
            asList(johnColtrane));

    public static final Album sampleShortAlbum = new Album("sample Short Album",
            asList(new Track("short track", 30)), asList(johnColtrane));

    public static final Album manyTrackAlbum = new Album("sample Many Track Album",
            asList(new Track("short track", 30), new Track("short track 2", 30),
                    new Track("short track 3", 30), new Track("short track 4", 30),
                    new Track("short track 5", 30)),
            asList(johnColtrane));

    public static Stream<Album> albums = Stream.of(aLoveSupreme);

    public static Stream<Artist> threeArtists() {
        return Stream.of(johnColtrane, johnLennon, theBeatles);
    }

    public static List<Artist> getThreeArtists() {
        return asList(johnColtrane, johnLennon, theBeatles);
    }

    public static final Album fourTrackAlbum = new Album("The Album",
            asList(new Track("a", 60), new Track("b", 60), new Track("c", 30), new Track("d", 90)),
            asList(new Artist("aa", "UK")));

    public static final Artist threeMemberBand = new Artist("Three-Member Band",
            asList(new Artist("3_1", "AU"), new Artist("3_2", "AU"), new Artist("3_3", "AU")),
            "AU");;
}
