package com.jashburn.javafeatures.java8.lambdas.lambdaconcurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.jashburn.javafeatures.java8.lambdas.Album;
import com.jashburn.javafeatures.java8.lambdas.Artist;
import com.jashburn.javafeatures.java8.lambdas.Track;
import org.junit.jupiter.api.Test;

/**
 * <ul>
 * <li>loginTo, lookupArtists, and lookupTracks all return a CompletableFuture instead of a
 * Future</li>
 * <li>key "trick" to the CompletableFuture API is to register lambda expressions and chain
 * higher-order functions</li>
 * </ul>
 */
class CompletableFuturesDemo {

    private static final int LOGIN_ARTIST_TIME_MS = 25;
    private static final int LOGIN_TRACKS_TIME_MS = 50;
    private static final int LOOKUP_ARTIST_TIME_MS = 75;
    private static final int LOOKUP_TRACKS_TIME_MS = 100;

    public Album lookupByName(String albumName) {
        // Use the thenCompose method to transform our Credentials into a CompletableFuture that
        // contains the artists
        CompletableFuture<List<Artist>> artistLookup =
                loginTo("artist").thenCompose(artistLogin -> lookupArtists(albumName, artistLogin));

        // (b) Generate a CompletableFuture of tracks
        CompletableFuture<List<Track>> tracksLookup =
                loginTo("track").thenCompose(trackLogin -> lookupTracks(albumName, trackLogin));

        // thenCombine takes the result from a CompletableFuture and combines it with another
        // CompletableFuture
        // - the combining operation is provided as a lambda expression
        // - take our tracks and artists and build up an Album object
        return tracksLookup.thenCombine(artistLookup,
                (tracks, artists) -> new Album(albumName, tracks, artists)).join();

        // join returns the result value when complete, or throws an (unchecked) exception if
        // completed exceptionally.
        // Like with the Streams API, we have not been actually doing things; we have been
        // building up a recipe that says how to do things until one of the final methods is called.
    }

    private CompletableFuture<Credentials> loginTo(String service) {
        // A very common usage of CompletableFuture is to asynchronously run a block of code that
        // returns a value
        // - supplyAsync: factory method for creating a CompletableFuture

        return CompletableFuture.supplyAsync(() -> {
            printMessage("[loginTo] Preparing login for " + service);

            // This Supplier can do some time-consuming work without blocking the current thread
            LoginService loginService = new LoginService();
            Credentials credentials = loginService.login(service);

            printMessage("[loginTo] Prepared login for " + service);

            // The value returned is used to complete the CompletableFuture
            return credentials;
        });

        // We can provide an Executor that tells the CompletableFuture where to perform the work.
        // E.g., CompletableFuture.supplyAsync(() -> { ... return credentials; }, executor);
        // When no Executor is provided, it just uses the same fork/join thread pool that parallel
        // streams execute on.
    }

    private class LoginService {

        Credentials login(String serviceName) {
            long loginTime = LOGIN_ARTIST_TIME_MS;
            if ("track".equals(serviceName)) {
                loginTime = LOGIN_TRACKS_TIME_MS;
            }

            printMessage("[login] Logging in for " + serviceName);

            try {
                TimeUnit.MILLISECONDS.sleep(loginTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            printMessage("[login] Logged in for " + serviceName);

            return new Credentials("Token: " + serviceName);
        }
    }

    private class Credentials {
        private String token;

        Credentials(String token) {
            this.token = token;
        }

        String getToken() {
            return token;
        }
    }

    private CompletableFuture<List<Artist>> lookupArtists(String albumName, Credentials login) {
        return CompletableFuture.supplyAsync(() -> {
            if (!login.getToken().equals("Token: artist")) {
                throw new IllegalArgumentException(login.getToken());
            }

            printMessage("[lookupArtists] Looking up for " + albumName);

            try {
                TimeUnit.MILLISECONDS.sleep(LOOKUP_ARTIST_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            printMessage("[lookupArtists] Looked up for " + albumName);

            return Arrays.asList(new Artist("aa", "UK"));
        });
    }

    private CompletableFuture<List<Track>> lookupTracks(String albumName, Credentials login) {
        return CompletableFuture.supplyAsync(() -> {
            if (!login.getToken().equals("Token: track")) {
                throw new IllegalArgumentException(login.getToken());
            }

            printMessage("[lookupTracks] Looking up up for " + albumName);

            try {
                TimeUnit.MILLISECONDS.sleep(LOOKUP_TRACKS_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            printMessage("[lookupTracks] Looked up up for " + albumName);

            return Arrays.asList(new Track("track 1", 100), new Track("track 2", 150));
        });
    }

    private long startTime = 0;

    private void printStart() {
        startTime = System.currentTimeMillis();
        System.out.println("Start");
    }

    private void printMessage(String msg) {
        System.out.println((System.currentTimeMillis() - startTime) + ": " + msg);
    }

    @Test
    void testLookupByName() {
        // Login should happen concurrently for both artist and track.
        // Artist and track lookups should happen concurrently.
        // Lookup starts once login is done without needing to wait for the other login.
        // Longest duration should be slightly longer than total of login and (the longer) lookup
        long maxDuration = LOOKUP_TRACKS_TIME_MS + LOGIN_TRACKS_TIME_MS + 50;

        printStart();
        Album album = assertTimeout(Duration.ofMillis(maxDuration), () -> lookupByName("The AA"));
        assertEquals("The AA", album.getName());
        assertEquals("aa", album.getMainMusician().getName());
        printMessage("End");
    }
    // Test output:
    // Start
    // 6: [loginTo] Preparing login for artist
    // 7: [loginTo] Preparing login for track
    // 7: [login] Logging in for track
    // 7: [login] Logging in for artist
    // 32: [login] Logged in for artist
    // 32: [loginTo] Prepared login for artist
    // 34: [lookupArtists] Looking up for The AA
    // 57: [login] Logged in for track
    // 57: [loginTo] Prepared login for track
    // 59: [lookupTracks] Looking up up for The AA
    // 110: [lookupArtists] Looked up for The AA
    // 160: [lookupTracks] Looked up up for The AA
    // 164: End
}
