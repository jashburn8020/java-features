package com.jashburn.javafeatures.java8.lambdas.lambda;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

/**
 * {@link Predicate} accepts an argument of type <code>T</code> and returns a boolean.
 * <p>
 * <code>T => Predicate => boolean</code>
 */
class PredicateDemo {

    private Predicate<Integer> lessThan5 = num -> num < 5;
    private Predicate<Integer> lessThan10 = num -> num < 10;
    private Predicate<Integer> greaterThan5 = num -> num > 5;
    private Predicate<Integer> greaterThan10 = num -> num > 10;

    @Test
    void singlePredicate() {
        assertTrue(lessThan10.test(9));
    }

    @Test
    void andOrPredicate() {
        assertTrue(greaterThan5.and(lessThan10).test(7));
        assertTrue(lessThan5.or(greaterThan10).test(3));
    }

    @Test
    void negateNotPredicate() {
        assertTrue(lessThan10.negate().test(10));
        assertTrue(Predicate.not(lessThan10).test(10));
    }

    @Test
    void isEqualPredicate() {
        Predicate<Integer> is7 = Predicate.isEqual(7);
        assertTrue(greaterThan5.and(lessThan10).and(is7).test(7));
    }
}
