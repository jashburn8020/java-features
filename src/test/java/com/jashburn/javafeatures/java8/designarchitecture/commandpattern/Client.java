package com.jashburn.javafeatures.java8.designarchitecture.commandpattern;

import java.util.HashMap;
import java.util.Map;

public class Client {

    private Editor editor;
    private Map<String, Macro> macros;

    Client() {
        editor = new Editor();
        macros = new HashMap<>();
    }

    /**
     * All our actions/commands are just lambda expressions; we can entirely dispense with concrete
     * classes for them.
     */
    void recordMacro() {
        String macroName = "OpenSaveClose";

        Macro macro = new Macro(macroName);
        macro.record(editor::open);
        macro.record(editor::save);
        macro.record(editor::close);

        macros.put(macroName, macro);
    }
}
