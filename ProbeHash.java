import java.util.Collection;
import java.util.Vector;

/*
 * Constructor Summary
ProbeHash()
Constructs a new, empty hash table, named hashTable, with an initial capacity of 10.
ProbeHash(Collection<E> list)
Constructs a new hash table, named containing the elements in the specified collection.
ProbeHash(int initialCapacity)
Constructs a new, empty set using the specified initial capacity.
Method Summary
void rehash()

Doubles the size of the hash table. This should be used in your add method
once your load factor is at or above .75

Hashing

Version 7 2

int hashFunction(E value)

Returns the index that the corresponding value should go to in the current
hash table.
boolean add(E value)

Adds the specified element to this hash set and returns true. If the value
already exists in the hash set then it is NOT added and false is returned.

boolean contains(E value)
Returns true if this hash set contains the specified value; false otherwise.
int length()
Returns the length of the Hash Table. In other words, total number of slots.

boolean isEmpty()
Return true if this hash set is empty, false otherwise.
boolean remove(E value)

Removes the specified value from this hash set. Returns true if successful and
false otherwise.

int size()
Returns the number of elements in this hash set.
String toString()
Returns a String representation of all the elements in this hash set.
 */

import java.util.Collection;
import java.util.Vector;

public class ProbeHash<E> implements Hash<E> {
    private Vector<E> hashTable;
    private Vector<Integer> statusTable; // Status table indicating whether each slot is removed, empty, or has an element
    private int capacity;
    private int size;
    private final double LOAD_FACTOR_THRESHOLD = 0.75;

    // Constructors
    public ProbeHash() {
        this.capacity = 10;
        this.size = 0;
        this.hashTable = new Vector<>(capacity);
        this.statusTable = new Vector<>(capacity);
        for (int i = 0; i < capacity; i++) {
            hashTable.add(null);
            statusTable.add(0); // Initialize status table with 0 (empty)
        }
    }

    public ProbeHash(Collection<E> list) {
        this();
        for (E element : list) {
            add(element);
        }
    }

    public ProbeHash(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        this.hashTable = new Vector<>(capacity);
        this.statusTable = new Vector<>(capacity);
        for (int i = 0; i < capacity; i++) {
            hashTable.add(null);
            statusTable.add(0); // Initialize status table with 0 (empty)
        }
    }

    // Helper method to calculate hash index
    private int hashFunction(E value) {
        return Math.abs(value.hashCode() % capacity);
    }

    // Method to double the size of the hash table
    private void rehash() {
        int newCapacity = capacity * 2;
        Vector<E> newHashTable = new Vector<>(newCapacity);
        Vector<Integer> newStatusTable = new Vector<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newHashTable.add(null);
            newStatusTable.add(0); // Initialize status table with 0 (empty)
        }
        for (int i = 0; i < capacity; i++) {
            if (hashTable.get(i) != null) {
                int newIndex = Math.abs(hashTable.get(i).hashCode() % newCapacity);
                while (newHashTable.get(newIndex) != null) {
                    newIndex = (newIndex + 1) % newCapacity; // Linear probing
                }
                newHashTable.set(newIndex, hashTable.get(i));
                newStatusTable.set(newIndex, 1); // Set status to 1 (has an element)
            }
        }
        hashTable = newHashTable;
        statusTable = newStatusTable;
        capacity = newCapacity;
    }

    // Implemented methods from Hash interface
     // return full length of hash table
    public int length() {
        return capacity;
    }

     // return instance var size
    public int size() {
        return size;
    }

     // check if its emptuy
    public boolean isEmpty() {
        return size == 0;
    }

// adding the given value to the hashTable     
    public boolean add(E value) {
        if (contains(value)) {
            // is it alr in the table return false
            return false;
        }
        if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            rehash();// is it is past the lf of .75 rehash
        }
        // otherwise find the index for the value
        int index = hashFunction(value);
        // while the value at given is not null
        while (hashTable.get(index) != null) {
            index = (index + 1) % capacity; // Linear probing
        }// once it is empty set the index to have the desired value u want to add
        hashTable.set(index, value);
        statusTable.set(index, 1); // Set status to 1 (has an element)
        // increase size
        size++;
        return true;
    }

    // Checks if the hash table contains the value
    public boolean contains(E value) {
        // find the index 
        int index = hashFunction(value);
        // initial or startingn index isn recorded by var
        int startIndex = index;
        do {
            // if the index is null and it is the value were looking for
            if (hashTable.get(index) != null && hashTable.get(index).equals(value)) {
                return true;
                // return ture
            }
            index = (index + 1) % capacity; // Linear probing
        } while (index != startIndex);
        // other wise return false
        return false;
    }

     // removwes desired val
    public boolean remove(E value) {
        // intial index found Using hash function
        int index = hashFunction(value);
        // Keep track of the intial index
        int startIndex = index;
        do {
            // if both the vale at index is null and it equals the desired removing value ...
            if (hashTable.get(index) != null && hashTable.get(index).equals(value)) {
                // set that index to null
                hashTable.set(index, null);
                statusTable.set(index, -1); // Set status to -1 (removed)
                // update size of elements
                size--;
                // succcessful remove
                return true;
            }
            index = (index + 1) % capacity; // Linear probing
        } while (index != startIndex);
        // otherwise return falswe
        return false;
    }

    // string builder
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < capacity; i++) {
            if (hashTable.get(i) != null) {
                sb.append(hashTable.get(i));
                sb.append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove trailing comma and space
        }
        sb.append("]");
        return sb.toString();
    }
}
