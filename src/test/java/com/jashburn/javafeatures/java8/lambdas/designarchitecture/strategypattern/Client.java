package com.jashburn.javafeatures.java8.lambdas.designarchitecture.strategypattern;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Client
 * <p>
 * Using either lambda expressions or method references allows us to remove a whole layer of
 * boilerplate code from this pattern. In this case, we can skip creating concrete strategy
 * implementations, and instead refer to methods that implement the algorithms.
 * <p>
 * Here the algorithms are represented by the constructors of the relevant OutputStream
 * implementation. The method (constructor) reference (e.g., <code>GZIPOutputStream::new</code>),
 * when called, corresponds to the {@link CompressionStrategy#compress(java.io.OutputStream)}
 * signature, i.e., {@link GZIPOutputStream#GZIPOutputStream(java.io.OutputStream)} accepts an
 * argument of type <code>OutputStream</code>, and returns an instance of type
 * <code>OutputStream</code> (which <code>GZIPOutputStream</code> is a subclass.) Therefore, when it
 * is executed in {@link Compressor#compress(Path, File)}, <code>strategy.compress(outStream)</code>
 * results in the creation of an instance of <code>GZIPOutputStream</code>, which is then passed in
 * as an argument to {@link Files#copy(Path, java.io.OutputStream)}.
 */
public class Client {

    void compressWithZip(Path inFile, File outFile) throws IOException {
        executeCompression(inFile, outFile, ZipOutputStream::new);
    }

    void compressWithGZIP(Path inFile, File outFile) throws IOException {
        executeCompression(inFile, outFile, GZIPOutputStream::new);
    }

    private void executeCompression(Path inFile, File outFile, CompressionStrategy strategy)
            throws IOException {
        Compressor compressor = new Compressor(strategy);
        compressor.compress(inFile, outFile);
    }
}
