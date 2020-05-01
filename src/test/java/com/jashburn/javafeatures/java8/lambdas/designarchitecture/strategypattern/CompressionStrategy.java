package com.jashburn.javafeatures.java8.lambdas.designarchitecture.strategypattern;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Strategy
 * <p>
 * Typical non-lambda implementation would involve separate classes that implement this interface.
 */
public interface CompressionStrategy {

    public OutputStream compress(OutputStream data) throws IOException;
}
