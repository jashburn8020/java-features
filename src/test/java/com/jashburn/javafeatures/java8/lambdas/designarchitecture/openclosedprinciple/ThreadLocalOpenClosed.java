package com.jashburn.javafeatures.java8.lambdas.designarchitecture.openclosedprinciple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link ThreadLocal#withInitial(java.util.function.Supplier)} method is a higher-order function
 * that takes a lambda expression that represents a factory for producing an initial value. We can
 * pass in a different factory method and get an instance of {@link ThreadLocal} with different
 * behaviour.
 */
class ThreadLocalOpenClosed {

    void differentBehaviourWithFactoryMethod() {

        ThreadLocal<DateFormat> localFormatter = ThreadLocal.withInitial(SimpleDateFormat::new);
        DateFormat formatter = localFormatter.get();

        /*
         * We can also generate completely different behavior by passing in a different lambda
         * expression
         */

        AtomicInteger threadId = new AtomicInteger();
        ThreadLocal<Integer> localId = ThreadLocal.withInitial(() -> threadId.getAndIncrement());
        int idForThisThread = localId.get();
    }
}
