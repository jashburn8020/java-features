package com.jashburn.javafeatures.java8.dataparallelism;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Performance {

    private static final int LIST_LENGTH = 1_000_000;

    private static List<Integer> arrayList = new ArrayList<>();
    private static List<Integer> linkedList = new LinkedList<>();

    @BeforeAll
    static void setUpLists() {
        List<Integer> list = IntStream.range(0, LIST_LENGTH).boxed().collect(Collectors.toList());
        arrayList.addAll(list);
        linkedList.addAll(list);
    }

    @Test
    void addIntegersArrayList() {
        long start = System.currentTimeMillis();
        addIntegers(arrayList);
        System.out.println("ArrayList: " + (System.currentTimeMillis() - start));
    }

    @Test
    void addIntegersLinkedList() {
        long start = System.currentTimeMillis();
        addIntegers(linkedList);
        System.out.println("LinkedList: " + (System.currentTimeMillis() - start));
    }

    private int addIntegers(List<Integer> values) {
        return values.parallelStream().mapToInt(i -> i).sum();
    }

}
