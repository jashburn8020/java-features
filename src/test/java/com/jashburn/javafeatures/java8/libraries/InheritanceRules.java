package com.jashburn.javafeatures.java8.libraries;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class InheritanceRules {

    public interface Parent {

        public default void welcome() {
            message("Parent");
        }

        public void message(String msg);

        public String getLastMessage();
    }

    class ParentImpl implements Parent {

        private String msg;

        @Override
        public void message(String msg) {
            this.msg = msg;
        }

        @Override
        public String getLastMessage() {
            return msg;
        }
    }

    class OverridingParent extends ParentImpl {

        @Override
        public void welcome() {
            message("OverridingParent");
        }
    }

    public interface Child extends Parent {

        @Override
        public default void welcome() {
            message("Child");
        }
    }

    class OverridingChild extends OverridingParent implements Child {
    }

    @Test
    void parentDefaultUsed() {
        Parent parent = new ParentImpl();
        parent.welcome();
        assertEquals("Parent", parent.getLastMessage());
    }

    @Test
    void concreteBeatsDefault() {
        assertAll(() -> {
            Child overridingChild = new OverridingChild();
            overridingChild.welcome();
            assertEquals("OverridingParent", overridingChild.getLastMessage());
        }, () -> {
            Parent overridingChild = new OverridingChild();
            overridingChild.welcome();
            assertEquals("OverridingParent", overridingChild.getLastMessage());
        });
    }
}
