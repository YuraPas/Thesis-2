package com.company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Denis Kazakov <http://94kazakov.github.io/>
 */
public class HashMapTest {

    private HashMap hashMap;
    private final int NUM_ELEMENTS = 20000;

    @Before
    public void setUp(){
        hashMap = new HashMap(20000);
    }


    @Test
    public void testSetGet(){
        hashMap.set("a", 1);
        assertEquals(1, hashMap.get("a"));
        hashMap.delete("a");
    }

    @Test
    public void testReplacement() {
        hashMap.set("b", 0);
        hashMap.set("b", 1);
        assertEquals(new Integer(1), hashMap.get("b"));
        hashMap.delete("b");
    }

    @Test
    public void testHashMapCollisions() {
        /* Initialise hashmap */
        for(int i = 0; i < NUM_ELEMENTS; i++){
            hashMap.set(Integer.toString(i), Integer.toString(i));
        }
        /* Test all values of the get method */
        for(int i = 0; i < NUM_ELEMENTS; i++){
            assertEquals(Integer.toString(i), hashMap.get(Integer.toString(i)));
        }

        for(int i = 0; i < NUM_ELEMENTS; i++){
            hashMap.delete(Integer.toString(i));
            assertEquals(null, hashMap.get(Integer.toString(i)));
        }
        assertEquals(0, hashMap.size);
    }

    @Test
    public void testDeletion() {
        String k = "TestsetGet";
        String v = "TestsetGetValue";
        hashMap.set(k, v);
        assertEquals(v, hashMap.delete(k));
        assertEquals(null , hashMap.get(k));
    }

    @After
    public void tearDown(){
        hashMap = null;
        System.gc();
    }
}


