package com.jashburn.javafeatures.java8.lambdas.designarchitecture.dependencyinversionprinciple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HeadingsExtractor {

    /**
     * Our heading-finding code is coupled with the resource-management and file-handling code.
     */
    List<String> findHeadings(Reader input) throws HeadingLookupException {
        try (BufferedReader reader = new BufferedReader(input)) {
            return reader.lines().filter(line -> line.endsWith(":"))
                    .map(line -> line.substring(0, line.length() - 1))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new HeadingLookupException(e);
        }
    }

    /**
     * What we really want to do is write some code that finds the headings and delegates the
     * details of a file to another method. We can use a {@code Stream<String>} as the abstraction
     * we want to depend upon rather than a file. A {@code Stream} is much safer and less open to
     * abuse. We also want to be able to a pass in a function that creates our domain exception if
     * there's a problem with the file.
     */
    List<String> findHeadingDependencyInversion(Reader input) throws HeadingLookupException {
        Function<Stream<String>, List<String>> handler = lines -> lines
                .filter(line -> line.endsWith(":"))
                .map(line -> line.substring(0, line.length() - 1))
                .collect(Collectors.toList());
        return withLinesOf(input, handler, HeadingLookupException::new);
    }

    /**
     * @param input   Takes in a reader that handles the underlying file I/O. This is wrapped up in
     *                {@code BufferedReader}, which lets us read the file line by line.
     * @param handler Represents the body of whatever code we want to use with this function. This
     *                function takes the {@code Stream} of the file's lines as its argument.
     * @param error   Called when there's an exception in the I/O code. Constructs a domain
     *                exception, which then gets thrown in the event of a problem.
     */
    private <T> T withLinesOf(Reader input, Function<Stream<String>, T> handler,
            Function<IOException, RuntimeException> error) {
        try (BufferedReader reader = new BufferedReader(input)) {
            return handler.apply(reader.lines());
        } catch (IOException e) {
            throw error.apply(e);
        }
    }
}

class HeadingLookupException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    HeadingLookupException(Exception e) {
        super(e);
    }
}