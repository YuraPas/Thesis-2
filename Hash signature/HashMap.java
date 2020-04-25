package com.company;
/**
 * HASHING FUNCTION:
 * In this implementation, I used Java's native hashCode() function, which
 * is: s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]                          [2]
 * [2]:http://docs.oracle.com/javase/6/docs/api/java/lang/String.html#hashCode()
 *
 * INTERPRETATION:
 * "Fixed-size hash map" was interpreted as that it holds a fixed number of
 * elements, however internal structure of the map can be modified. Therefore,
 * we don't need to implement methods of:
 * double() - doubling size of our hash map,
 * rehash() - rehash our map into a bigger sized array to preserve uniform
 * distribution of elements.
 *
 * COLLISION RESOLUTION:
 * In this implementation, I am using Chained Hashing. At a given key, Chained
 * hashing stores a head reference to a linked list of keys (a "bucket").
 *
 * COMPLEXITY:
 *     When hashing function is evenly distributed across the length of the
 *     array, runtime performance doesn't suffer much, because we are protected
 *     probabilistically from the "clustering effect" (when we keep storing
 *     values at specific "cluster" of buckets, ignoring the empty buckets).
 *     Thus, our performance for set(), get(), delete() should probabilistically
 *     remain O(1). Worst-case complexity O(N), which would happen
 *     if all keys produced the same hash value.
 *
 * NOTE:(choice of collision resolution)
 *     There are two main ways to resolve collision conflicts in the hash map:
 *     1) Sampling methods:
 *            If collision occurs, find a bin that is not taken. This approach
 *            suffers immensely when our load factor becomes substantially large.
 *            Example: linear probing performance
 *                ? = occupied_Cells/ARRAY_SIZE = load_Factor
 *                average lookup time is: (1 + 1/(1-?)) / 2                  [1]
 *     2) Chaining method:
 *             If a collision occurs on a given key, we just add the conflicting
 *             value onto the linked list
 *             that exists at a given key
 *             Example: chaining hashing performance
 *                 average lookup time is: 1 + ?/2                           [1]
 *     Therefore, we conclude that we are better off using Chaining method,
 *     because it will maintain decent performance even when the load factor
 *     becomes large.
 *     Reference:
 *     [1]:slide 9, https://www.cs.cmu.edu/~tcortina/15-121sp10/Unit08B.pdf
 *
 * NOTE:(Java's garbage collection)
 *     Since Java automatically collects and deallocates elements that are not
 *     reachable, we don't have to worry about malloc and dealloc as long as we
 *     properly delete elements.
 *
 * NOTE:(importance of rehashing)
 *     We need to "rehash" existing elements of the hash table
 *     into the bigger sized hash map to keep hash map evenly distributed.
 * @author Denis Kazakov <http://94kazakov.github.io/>\
 * <T> - arbitrary object type
 */
public class HashMap<T> {
    private int ARRAY_SIZE;
    private NodeLinkedList<T>[] buckets;
    int size; //counter for number of elements in the hashmap

    public HashMap(int size){
        this.size = 0;
        if (size > 0){
            ARRAY_SIZE = size;
            buckets = new NodeLinkedList[ARRAY_SIZE];
        } else{
            throw new IllegalArgumentException("Please set hash map size to be nonnegative" + size);
        }
    }

    /**
     * Insert the key-value pair into the hash map array.
     * @param key - used to uniquely identify the key-value pair
     * @param value - reference to an object that we want to map
     */
    public boolean set(String key, T value){
        if (value == null || key == null){
            return false;
        }
        int hash = hash(key);
        if (this.load() == 1) {
            /*load value of 0.75 would have been used to trigger doubling the size and rehashing
              if we were building hash map that is not fixed size*/
            System.out.println("You have exceeded the capacity of a hash map you declared. Use .delete() to remove elements");
            return false;
        }
        if (buckets[hash] == null) {
            buckets[hash] = new NodeLinkedList<>();
            buckets[hash].set(key, value);
        } else {
            NodeLinkedList<T> NodeLinkedList = buckets[hash];
            NodeLinkedList.set(key, value);
        }
        return true;
    }

    /**
     * Returns the value that is mapped to the given key.
     * @param key - used to search for a value in hash map
     * @return value - reference to an object that we mapped
     */
    public T get(String key){
        if (key == null){
            throw new IllegalArgumentException();
        }
        int hash = hash(key);
        if (buckets[hash] != null){
            return buckets[hash].get(key);
        }
        return null;
    }

    /**
     * Deletes the element for a given key and returns it.
     * @param key - used to search for a value in hash map
     * @return value - reference to an object that we mapped, null - if element didn't exist in the hash map
     */
    public T delete(String key) {
        if (key == null){
            throw new IllegalArgumentException();
        }
        int hash = hash(key);
        if (buckets[hash] != null) {
            return buckets[hash].delete(key);
        }
        return null;
    }


    /**
     * @return float value representing the load factor (`(items in hash map)/(size of hash map)`) of the data structure.
     */
    public float load(){
        return (float) (size * 1.0 / buckets.length);
    }

    /**
     * @param key - used to uniquely identify the key-value pair
     * @return hash value for a given key
     */
    private int hash(String key){
        return Math.abs(key.hashCode() % ARRAY_SIZE);
    }

    /**
     * Singly Linked list class of Nodes that contain an arbitrary type object reference, key, next pointer.
     */
    private class NodeLinkedList<T> {
        Node<T> head;

        public Node<T> getHead() {
            return head;
        }

        public void setHead(Node<T> head) {
            this.head = head;
        }

        /**
         * Insert the key-value pair into the linked list bucket. Because I chose chaining method, it is sufficient
         * to only check for duplicate keys in whatever bucket our hash function produced.
         * @param key - used to uniquely identify the key-value pair
         * @param value - reference to an object that we want to map
         */
        private boolean set(String key, T value){
            Node<T> input = new Node(key, value);
            //insert input if bucket is empty
            if(head == null){
                size++;
                head = input;
            }else{
                //collision occurred, therefore we append input variable to linked list
                Node<T> runner = head;
                while(runner != null){
                    //if given key already exists in the hash map, then we need to replace it to ensure no keys are repeated
                    if(runner.getKey().equals(key)){
                        runner.setValue(value);
                        return true;
                    }
                    runner = runner.getNext();
                }
                //given key is new and is appended onto the beginning of the bucket's linked list
                input.setNext(head);
                head = input;
                size++;
            }
            return true;
        }

        /**
         * Deletes the element for a given key and returns it.
         * @param key - used to search for a value in hash map
         * @return value - reference to an object that we mapped, null - if element didn't exist in the hash map
         */
        private T delete(String key){
            int hash = hash(key);
            //get the head of linked list
            Node<T> runner = head;
            //if head is the key we are looking for, assign buckets[key] to a new head that is next element
            if(runner.getKey().equals(key)){
                head = runner.getNext();
                size --;
                return runner.getValue();
            }
            //traverse linked list
            while(runner.getNext() != null){
                //if next element is what we are looking for, then we have to reassign the current pointer to the element after next
                Node<T> next = runner.getNext();
                if(next.getKey().equals(key)){
                    runner.setNext(next.getNext());
                    size--;
                    return next.getValue();
                }
                runner = runner.getNext();
            }
            //if key wasn't set:
            return null;
        }

        /**
         * Returns the value that is mapped to the given key.
         * @param key - used to search for a value in hash map
         * @return value - reference to an object that we mapped
         */
        private T get(String key){
            int hash = hash(key);
            //get the head of linked list
            Node<T> runner = head;
            //traverse linked list
            while(runner != null){
                if(runner.getKey().equals(key)){
                    return runner.getValue();
                }
                runner = runner.getNext();
            }
            //if key wasn't set:
            return null;
        }

    }

    /**
     * Node is a key-value object that we store in our hash map, where value is
     * just a reference to an object. Node object has key, value, pointer to
     * the next element of bucket's linked list.
     */
    private class Node<T> {
        private String key;
        private T value;
        private Node next;

        public Node(){
        }

        public Node(String key, T value){
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}