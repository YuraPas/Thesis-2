package com.company;

public class Main {
    public static void main(String[] args) {
        HashMapTest test = new HashMapTest();
        test.setUp();
        System.out.println("Testing for basic Set, Get commands");
        test.testSetGet();
        System.out.println("Testing for basic deletion");
        test.testDeletion();
        System.out.println("Testing for replacement");
        test.testReplacement();
        System.out.println("Input 20000 elements to test on collisions, delete afterwards, check size to be 0");
        test.testHashMapCollisions();

    }
}
