package com.jashburn.javafeatures.java8.lambdas.designarchitecture.observerpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Subject
 */
class Moon {

    private final List<LandingObserver> observers = new ArrayList<>();
    private final List<Consumer<String>> consumerObservers = new ArrayList<>();

    void land(String name) {
        observers.forEach(observer -> observer.observeLanding(name));

        consumerObservers.forEach(observer -> observer.accept(name));
    }

    void startSpying(LandingObserver observer) {
        observers.add(observer);
    }

    void startSpyingUsingConsumer(Consumer<String> observer) {
        consumerObservers.add(observer);
    }
}
