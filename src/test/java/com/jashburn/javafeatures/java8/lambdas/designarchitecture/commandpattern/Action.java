package com.jashburn.javafeatures.java8.lambdas.designarchitecture.commandpattern;

/**
 * Command
 * <p>
 * Perform a single action on {@link Editor}.
 * <p>
 * Typical non-lambda implementation would involve separate classes that implement this interface.
 */
public interface Action {

    public void perform();
}
