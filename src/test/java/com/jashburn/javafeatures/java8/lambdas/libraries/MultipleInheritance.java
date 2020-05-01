package com.jashburn.javafeatures.java8.lambdas.libraries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MultipleInheritance {

    public interface Carriage {
        public default String rock() {
            return "... from side to side";
        }
    }

    public interface Jukebox {
        public default String rock() {
            return "... around the world";
        }
    }

    class MusicalCarriage implements Carriage, Jukebox {
        @Override
        public String rock() {
            return Jukebox.super.rock();
        }
    }

    @Test
    void enhancedSuperMultipleInheritance() {
        Carriage musicalCarriage = new MusicalCarriage();
        assertEquals("... around the world", musicalCarriage.rock());
    }
}
