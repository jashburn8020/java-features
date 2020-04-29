package com.jashburn.javafeatures.java8.designarchitecture.commandpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Invoker
 * <p>
 * Record actions and run them as a group.
 */
public class Macro {

    private final List<Action> actions;
    private final String name;

    public Macro(String name) {
        this.actions = new ArrayList<>();
        this.name = name;
    }

    public void record(Action action) {
        actions.add(action);
    }

    public void run() {
        actions.forEach(Action::perform);
    }
}
