package com.jashburn.javafeatures.java8.lambdas.designarchitecture.observerpattern;

import java.util.function.Consumer;

/**
 * By using lambdas, we skip writing boilerplate classes that implement the specific observers.
 */
class Client {

    private Moon moon;

    Client() {
        moon = new Moon();
    }

    void spy() {
        LandingObserver nasaObserver = name -> {
            if (name.equals("Apollo")) {
                System.out.println("We've made it!");
            }
        };
        moon.startSpying(nasaObserver);

        LandingObserver alienObserver = name -> {
            if (name.equals("meteorite")) {
                System.out.println("Dinner time!");
            }
        };
        moon.startSpying(alienObserver);
    }

    /**
     * By using {@link Consumer}, we can even skip writing the observer interface.
     */
    void spyUsingConsumer() {
        Consumer<String> nasaObserver = name -> {
            if (name.equals("Apollo")) {
                System.out.println("We've made it!");
            }
        };
        moon.startSpyingUsingConsumer(nasaObserver);

        Consumer<String> alienObserver = name -> {
            if (name.equals("meteorite")) {
                System.out.println("Dinner time!");
            }
        };
        moon.startSpyingUsingConsumer(alienObserver);
    }

    void landStuff() {
        moon.land("Apollo");
        moon.land("meteorite");
    }
}
